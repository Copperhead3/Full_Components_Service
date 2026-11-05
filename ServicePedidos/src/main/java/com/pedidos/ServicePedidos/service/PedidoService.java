package com.pedidos.ServicePedidos.service;

import com.pedidos.ServicePedidos.dto.*;
import com.pedidos.ServicePedidos.model.DetallePedido;
import com.pedidos.ServicePedidos.model.EstadoPedido;
import com.pedidos.ServicePedidos.model.Pedido;
import com.pedidos.ServicePedidos.repository.PedidoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.carrito.url}")
    private String carritoUrl;

    @Value("${services.inventario.url}")
    private String inventarioUrl;

    @Value("${services.despacho.url}")
    private String despachoUrl;

    @Value("${services.pago.url}")
    private String pagoUrl;

    public Pedido crearPedido(CrearPedidoRequest request) {

        validarModalidadEntrega(request);
        validarCarritoParaCheckout(request.getIdCliente());

        CarritoDto carrito = validarPrecios(request.getIdCliente());

        if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        Pedido pedido = construirPedido(request.getIdCliente(), carrito);
        pedido = pedidoRepository.saveAndFlush(pedido);

        try {
            reservarInventario(pedido);

            DespachoDto despacho = crearDespacho(
                    pedido.getIdPedido(),
                    request.getModalidadEntrega(),
                    request.getDireccion()
            );

            pedido.setIdDespacho(despacho.getIdDespacho());
            pedido.setCostoDespacho(
                    despacho.getCosto() == null ? BigDecimal.ZERO : despacho.getCosto()
            );
            pedido.setTotal(pedido.getSubtotal().add(pedido.getCostoDespacho()));
            pedidoRepository.saveAndFlush(pedido);

            PagoDto pagoCreado = crearPago(
                    pedido.getIdPedido(),
                    pedido.getTotal(),
                    request.getMetodoPago()
            );

            pedido.setIdPago(pagoCreado.getIdPago());
            pedidoRepository.saveAndFlush(pedido);

            PagoDto pagoIniciado = iniciarPago(pagoCreado.getIdPago());

            if (!"APROBADO".equalsIgnoreCase(pagoIniciado.getEstado())) {
                compensarPedidoFallido(pedido);
                throw new RuntimeException("El pago no fue aprobado");
            }

            confirmarReservas(pedido);

            pedido.setEstado(EstadoPedido.CONFIRMADO);
            pedido = pedidoRepository.saveAndFlush(pedido);

            vaciarCarrito(request.getIdCliente());

            return pedido;

        } catch (RuntimeException ex) {

            if (pedido.getEstado() != EstadoPedido.CONFIRMADO) {
                compensarPedidoFallido(pedido);
            }

            throw ex;
        }
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Long idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<Pedido> obtenerPorCliente(Long idCliente) {
        return pedidoRepository.findByIdClienteOrderByFechaPedidoDesc(idCliente);
    }

    public List<Pedido> obtenerPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public Pedido actualizarEstado(Long idPedido, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPorId(idPedido);

        if (nuevoEstado == EstadoPedido.CANCELADO) {
            return cancelarPedido(idPedido);
        }

        validarTransicion(pedido.getEstado(), nuevoEstado);
        pedido.setEstado(nuevoEstado);

        return pedidoRepository.save(pedido);
    }

    public Pedido cancelarPedido(Long idPedido) {
        Pedido pedido = obtenerPorId(idPedido);

        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new RuntimeException("El pedido ya está cancelado");
        }

        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            throw new RuntimeException("Un pedido entregado no puede ser cancelado");
        }

        if (pedido.getIdPago() != null) {
            intentarAnularPago(pedido.getIdPago());
        }

        liberarReservas(pedido);

        pedido.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    private Pedido construirPedido(Long idCliente, CarritoDto carrito) {

        Pedido pedido = new Pedido();
        pedido.setIdCliente(idCliente);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setCostoDespacho(BigDecimal.ZERO);

        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemCarritoDto item : carrito.getItems()) {

            if (item.getIdProducto() == null ||
                item.getCantidad() == null ||
                item.getCantidad() <= 0 ||
                item.getPrecioUnitario() == null) {

                throw new RuntimeException("El carrito contiene un producto inválido");
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setIdProducto(item.getIdProducto());
            detalle.setNombreProducto(
                    item.getNombreProducto() == null
                            ? "Producto " + item.getIdProducto()
                            : item.getNombreProducto()
            );
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());

            BigDecimal detalleSubtotal =
                    item.getPrecioUnitario()
                            .multiply(BigDecimal.valueOf(item.getCantidad()));

            detalle.setSubtotal(detalleSubtotal);
            detalle.setPedido(pedido);

            detalles.add(detalle);
            subtotal = subtotal.add(detalleSubtotal);
        }

        pedido.setDetalles(detalles);
        pedido.setSubtotal(subtotal);
        pedido.setTotal(subtotal);

        return pedido;
    }

    private void validarCarritoParaCheckout(Long idCliente) {
        try {
            Boolean valido = restTemplate.getForObject(
                    carritoUrl + "/api/v1/carritos/cliente/"
                            + idCliente + "/validar-checkout",
                    Boolean.class
            );

            if (!Boolean.TRUE.equals(valido)) {
                throw new RuntimeException("El carrito no está listo para checkout");
            }

        } catch (RestClientException ex) {
            throw new RuntimeException(
                    "No fue posible validar el carrito para checkout"
            );
        }
    }

    private CarritoDto validarPrecios(Long idCliente) {
        try {
            ResponseEntity<CarritoDto> response = restTemplate.exchange(
                    carritoUrl + "/api/v1/carritos/cliente/"
                            + idCliente + "/validar-precios",
                    HttpMethod.PUT,
                    HttpEntity.EMPTY,
                    CarritoDto.class
            );

            return response.getBody();

        } catch (RestClientException ex) {
            throw new RuntimeException(
                    "No fue posible validar los precios del carrito"
            );
        }
    }

    private void reservarInventario(Pedido pedido) {

        for (DetallePedido detalle : pedido.getDetalles()) {

            ReservaStockRequest request = new ReservaStockRequest();
            request.setIdProducto(detalle.getIdProducto());
            request.setCantidadReserva(detalle.getCantidad());
            request.setReferenciaCompra(
                    "PEDIDO-" + pedido.getIdPedido()
                            + "-PRODUCTO-" + detalle.getIdProducto()
            );

            try {
                ReservaStockDto reserva = restTemplate.postForObject(
                        inventarioUrl + "/api/v1/inventario/reservas",
                        request,
                        ReservaStockDto.class
                );

                if (reserva == null || reserva.getIdReservaStock() == null) {
                    throw new RuntimeException("Inventario no creó la reserva");
                }

                detalle.setIdReservaStock(reserva.getIdReservaStock());

            } catch (RestClientException ex) {
                throw new RuntimeException(
                        "No fue posible reservar stock para el producto "
                                + detalle.getIdProducto()
                );
            }
        }

        pedidoRepository.saveAndFlush(pedido);
    }

    private void confirmarReservas(Pedido pedido) {

        for (DetallePedido detalle : pedido.getDetalles()) {

            if (detalle.getIdReservaStock() == null) {
                continue;
            }

            try {
                restTemplate.postForObject(
                        inventarioUrl
                                + "/api/v1/inventario/reservas/"
                                + detalle.getIdReservaStock()
                                + "/confirmar",
                        null,
                        ReservaStockDto.class
                );

            } catch (RestClientException ex) {
                throw new RuntimeException(
                        "No fue posible confirmar la reserva de stock "
                                + detalle.getIdReservaStock()
                );
            }
        }
    }

    private void liberarReservas(Pedido pedido) {

        for (DetallePedido detalle : pedido.getDetalles()) {

            if (detalle.getIdReservaStock() == null) {
                continue;
            }

            try {
                restTemplate.postForObject(
                        inventarioUrl
                                + "/api/v1/inventario/reservas/"
                                + detalle.getIdReservaStock()
                                + "/liberar",
                        null,
                        ReservaStockDto.class
                );
            } catch (Exception ignored) {
            }
        }
    }

    private DespachoDto crearDespacho(
            Long idPedido,
            String modalidad,
            DireccionEntregaDto direccion) {

        CrearDespachoRequest request = new CrearDespachoRequest();
        request.setIdPedido(idPedido);
        request.setModalidad(modalidad);
        request.setDireccion(direccion);

        try {
            DespachoDto despacho = restTemplate.postForObject(
                    despachoUrl + "/api/v1/despachos",
                    request,
                    DespachoDto.class
            );

            if (despacho == null || despacho.getIdDespacho() == null) {
                throw new RuntimeException("No fue posible crear el despacho");
            }

            return despacho;

        } catch (RestClientException ex) {
            throw new RuntimeException(
                    "Error al comunicarse con el microservicio Despacho"
            );
        }
    }

    private PagoDto crearPago(
            Long idPedido,
            BigDecimal monto,
            String metodoPago) {

        CrearPagoRequest request = new CrearPagoRequest();
        request.setIdPedido(idPedido);
        request.setMonto(monto);
        request.setMoneda("CLP");
        request.setMetodo(metodoPago);

        try {
            PagoDto pago = restTemplate.postForObject(
                    pagoUrl + "/api/v1/pagos",
                    request,
                    PagoDto.class
            );

            if (pago == null || pago.getIdPago() == null) {
                throw new RuntimeException("No fue posible crear el pago");
            }

            return pago;

        } catch (RestClientException ex) {
            throw new RuntimeException(
                    "Error al comunicarse con el microservicio Pago"
            );
        }
    }

    private PagoDto iniciarPago(Long idPago) {
        try {
            PagoDto pago = restTemplate.postForObject(
                    pagoUrl + "/api/v1/pagos/" + idPago + "/iniciar",
                    null,
                    PagoDto.class
            );

            if (pago == null) {
                throw new RuntimeException("Pago no respondió al iniciar");
            }

            return pago;

        } catch (RestClientException ex) {
            throw new RuntimeException(
                    "No fue posible iniciar el pago"
            );
        }
    }

    private void vaciarCarrito(Long idCliente) {
        try {
            restTemplate.delete(
                    carritoUrl + "/api/v1/carritos/cliente/"
                            + idCliente + "/items"
            );
        } catch (Exception ignored) {
        }
    }

    private void intentarAnularPago(Long idPago) {
        try {
            restTemplate.postForObject(
                    pagoUrl + "/api/v1/pagos/" + idPago + "/anular",
                    null,
                    PagoDto.class
            );
        } catch (Exception ignored) {
        }
    }

    private void compensarPedidoFallido(Pedido pedido) {

        if (pedido.getIdPago() != null) {
            intentarAnularPago(pedido.getIdPago());
        }

        liberarReservas(pedido);

        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    private void validarModalidadEntrega(CrearPedidoRequest request) {

        if ("DOMICILIO".equalsIgnoreCase(request.getModalidadEntrega())
                && request.getDireccion() == null) {

            throw new RuntimeException(
                    "La dirección es obligatoria para entrega a domicilio"
            );
        }
    }

    private void validarTransicion(
            EstadoPedido actual,
            EstadoPedido nuevo) {

        if (actual == nuevo) {
            throw new RuntimeException(
                    "El pedido ya se encuentra en ese estado"
            );
        }

        boolean permitida = switch (actual) {
            case PENDIENTE ->
                    nuevo == EstadoPedido.CONFIRMADO;

            case CONFIRMADO ->
                    nuevo == EstadoPedido.EN_PREPARACION;

            case EN_PREPARACION ->
                    nuevo == EstadoPedido.ENVIADO;

            case ENVIADO ->
                    nuevo == EstadoPedido.ENTREGADO;

            case ENTREGADO, CANCELADO -> false;
        };

        if (!permitida) {
            throw new RuntimeException(
                    "Transición de estado no permitida: "
                            + actual + " -> " + nuevo
            );
        }
    }
}
