package com.inventario.ServiceInventario.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.inventario.ServiceInventario.dto.AjusteStockRequest;
import com.inventario.ServiceInventario.dto.ProductoDto;
import com.inventario.ServiceInventario.dto.ReservaStockRequest;
import com.inventario.ServiceInventario.model.AlertaStock;
import com.inventario.ServiceInventario.model.EstadoAlerta;
import com.inventario.ServiceInventario.model.EstadoReserva;
import com.inventario.ServiceInventario.model.MovimientoStock;
import com.inventario.ServiceInventario.model.ReservaStock;
import com.inventario.ServiceInventario.model.Stock;
import com.inventario.ServiceInventario.model.TipoMovimiento;
import com.inventario.ServiceInventario.repository.AlertaStockRepository;
import com.inventario.ServiceInventario.repository.MovimientoStockRepository;
import com.inventario.ServiceInventario.repository.ReservaStockRepository;
import com.inventario.ServiceInventario.repository.StockRepository;

@Service
public class InventarioService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ReservaStockRepository reservaStockRepository;
    @Autowired
    private MovimientoStockRepository movimientoStockRepository;
    @Autowired
    private AlertaStockRepository alertaStockRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.producto.url}")
    private String productoUrl;

    @Transactional
    public Stock registrarStock(Stock stock) {
        validarProducto(stock.getIdProducto());
        if (stockRepository.existsByIdProducto(stock.getIdProducto())) {
            throw new RuntimeException("Ya existe un registro de stock para el producto indicado.");
        }
        if (stock.getCantidadFisica() == null) stock.setCantidadFisica(0);
        if (stock.getCantidadReservada() == null) stock.setCantidadReservada(0);
        if (stock.getStockMinimo() == null) stock.setStockMinimo(0);
        if (stock.getCantidadReservada() > stock.getCantidadFisica()) {
            throw new RuntimeException("La cantidad reservada no puede superar la cantidad física.");
        }
        Stock guardado = stockRepository.save(stock);
        registrarMovimiento(guardado, TipoMovimiento.REGISTRO, guardado.getCantidadFisica(), 0,
                guardado.getCantidadFisica(), "REGISTRO-INICIAL");
        revisarAlerta(guardado);
        return guardado;
    }

    public List<Stock> listarStock() {
        return stockRepository.findAll();
    }

    public Stock buscarStock(Long idStock) {
        return stockRepository.findById(idStock)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado."));
    }

    public Stock buscarPorProducto(Long idProducto) {
        return stockRepository.findByIdProducto(idProducto)
                .orElseThrow(() -> new RuntimeException("No existe stock registrado para el producto."));
    }

    public List<Stock> listarStockBajo() {
        return stockRepository.findConStockBajo();
    }

    @Transactional
    public Stock ajustarStock(Long idStock, AjusteStockRequest request) {
        Stock stock = buscarStock(idStock);
        int anterior = stock.getCantidadFisica();
        int posterior;

        if (request.getTipo() == TipoMovimiento.ENTRADA || request.getTipo() == TipoMovimiento.AJUSTE_POSITIVO) {
            posterior = anterior + request.getCantidad();
        } else if (request.getTipo() == TipoMovimiento.AJUSTE_NEGATIVO) {
            posterior = anterior - request.getCantidad();
        } else {
            throw new RuntimeException("Para ajustes manuales solo se permiten ENTRADA, AJUSTE_POSITIVO o AJUSTE_NEGATIVO.");
        }

        if (posterior < stock.getCantidadReservada()) {
            throw new RuntimeException("El ajuste dejaría el stock físico por debajo del stock reservado.");
        }

        stock.setCantidadFisica(posterior);
        Stock guardado = stockRepository.save(stock);
        registrarMovimiento(guardado, request.getTipo(), request.getCantidad(), anterior, posterior,
                request.getReferenciaOperacion());
        revisarAlerta(guardado);
        return guardado;
    }

    @Transactional
    public Stock cambiarStockMinimo(Long idStock, Integer stockMinimo) {
        if (stockMinimo == null || stockMinimo < 0) {
            throw new RuntimeException("El stock mínimo no puede ser negativo.");
        }
        Stock stock = buscarStock(idStock);
        stock.setStockMinimo(stockMinimo);
        Stock guardado = stockRepository.save(stock);
        revisarAlerta(guardado);
        return guardado;
    }

    @Transactional
    public ReservaStock reservar(ReservaStockRequest request) {
        validarProducto(request.getIdProducto());
        Stock stock = buscarPorProducto(request.getIdProducto());
        int disponible = stock.getCantidadFisica() - stock.getCantidadReservada();
        if (disponible < request.getCantidadReserva()) {
            throw new RuntimeException("Stock disponible insuficiente para realizar la reserva.");
        }

        ReservaStock reserva = new ReservaStock();
        reserva.setIdProducto(request.getIdProducto());
        reserva.setReferenciaCompra(request.getReferenciaCompra());
        reserva.setCantidadReserva(request.getCantidadReserva());
        reserva.setEstadoReserva(EstadoReserva.ACTIVA);
        reserva.setFechaCreacionReserva(LocalDateTime.now());
        reserva.setVenceEn(LocalDateTime.now().plusMinutes(30));
        reserva.setStock(stock);

        stock.setCantidadReservada(stock.getCantidadReservada() + request.getCantidadReserva());
        stockRepository.save(stock);
        ReservaStock guardada = reservaStockRepository.save(reserva);
        revisarAlerta(stock);
        return guardada;
    }

    public ReservaStock consultarReserva(Long idReserva) {
        return reservaStockRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva de stock no encontrada."));
    }

    @Transactional
    public ReservaStock confirmarReserva(Long idReserva) {
        ReservaStock reserva = consultarReserva(idReserva);
        if (reserva.getEstadoReserva() != EstadoReserva.ACTIVA) {
            throw new RuntimeException("Solo se pueden confirmar reservas activas.");
        }
        if (reserva.getVenceEn().isBefore(LocalDateTime.now())) {
            expirarReserva(reserva);
            throw new RuntimeException("La reserva se encuentra expirada.");
        }

        Stock stock = reserva.getStock();
        int anterior = stock.getCantidadFisica();
        int posterior = anterior - reserva.getCantidadReserva();
        if (posterior < 0) throw new RuntimeException("El stock físico es insuficiente.");

        stock.setCantidadFisica(posterior);
        stock.setCantidadReservada(Math.max(0, stock.getCantidadReservada() - reserva.getCantidadReserva()));
        reserva.setEstadoReserva(EstadoReserva.CONFIRMADA);
        stockRepository.save(stock);
        ReservaStock guardada = reservaStockRepository.save(reserva);
        registrarMovimiento(stock, TipoMovimiento.VENTA, reserva.getCantidadReserva(), anterior, posterior,
                reserva.getReferenciaCompra());
        revisarAlerta(stock);
        return guardada;
    }

    @Transactional
    public ReservaStock liberarReserva(Long idReserva) {
        ReservaStock reserva = consultarReserva(idReserva);
        if (reserva.getEstadoReserva() != EstadoReserva.ACTIVA) {
            throw new RuntimeException("Solo se pueden liberar reservas activas.");
        }
        Stock stock = reserva.getStock();
        stock.setCantidadReservada(Math.max(0, stock.getCantidadReservada() - reserva.getCantidadReserva()));
        reserva.setEstadoReserva(EstadoReserva.LIBERADA);
        stockRepository.save(stock);
        ReservaStock guardada = reservaStockRepository.save(reserva);
        revisarAlerta(stock);
        return guardada;
    }

    @Transactional
    public int expirarReservas() {
        List<ReservaStock> expiradas = reservaStockRepository
                .findByVenceEnBeforeAndEstadoReserva(LocalDateTime.now(), EstadoReserva.ACTIVA);
        for (ReservaStock reserva : expiradas) expirarReserva(reserva);
        return expiradas.size();
    }

    public List<MovimientoStock> listarMovimientos(Long idStock) {
        buscarStock(idStock);
        return movimientoStockRepository.findByStock_IdStockOrderByFechaDesc(idStock);
    }

    public List<AlertaStock> listarAlertas() {
        return alertaStockRepository.findAll();
    }

    public List<AlertaStock> listarAlertasActivas() {
        return alertaStockRepository.findByEstadoAlerta(EstadoAlerta.ACTIVA);
    }

    @Transactional
    public AlertaStock resolverAlerta(Long idAlerta) {
        AlertaStock alerta = alertaStockRepository.findById(idAlerta)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada."));
        alerta.setEstadoAlerta(EstadoAlerta.RESUELTA);
        alerta.setResueltaEn(LocalDateTime.now());
        return alertaStockRepository.save(alerta);
    }

    private void validarProducto(Long idProducto) {
        try {
            String url = productoUrl + "/api/v1/productos/buscar/" + idProducto;
            ResponseEntity<ProductoDto> respuesta = restTemplate.getForEntity(url, ProductoDto.class);
            if (!respuesta.getStatusCode().is2xxSuccessful() || respuesta.getBody() == null) {
                throw new RuntimeException("Producto no encontrado en el microservicio Producto.");
            }
        } catch (RestClientException ex) {
            throw new RuntimeException("No fue posible validar el producto con el microservicio Producto.");
        }
    }

    private void registrarMovimiento(Stock stock, TipoMovimiento tipo, Integer cantidad, Integer anterior,
            Integer posterior, String referencia) {
        MovimientoStock movimiento = new MovimientoStock();
        movimiento.setStock(stock);
        movimiento.setTipo(tipo);
        movimiento.setCantidadMovimiento(cantidad);
        movimiento.setStockAnterior(anterior);
        movimiento.setStockPosterior(posterior);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setReferenciaOperacion(referencia);
        movimientoStockRepository.save(movimiento);
    }

    private void revisarAlerta(Stock stock) {
        int disponible = stock.getCantidadFisica() - stock.getCantidadReservada();
        var activa = alertaStockRepository.findFirstByStock_IdStockAndEstadoAlerta(stock.getIdStock(), EstadoAlerta.ACTIVA);
        if (disponible <= stock.getStockMinimo()) {
            if (activa.isEmpty()) {
                AlertaStock alerta = new AlertaStock();
                alerta.setStock(stock);
                alerta.setEstadoAlerta(EstadoAlerta.ACTIVA);
                alerta.setCantidadDetectada(disponible);
                alerta.setFechaCreacionAlerta(LocalDateTime.now());
                alertaStockRepository.save(alerta);
            }
        } else if (activa.isPresent()) {
            AlertaStock alerta = activa.get();
            alerta.setEstadoAlerta(EstadoAlerta.RESUELTA);
            alerta.setResueltaEn(LocalDateTime.now());
            alertaStockRepository.save(alerta);
        }
    }

    private void expirarReserva(ReservaStock reserva) {
        Stock stock = reserva.getStock();
        stock.setCantidadReservada(Math.max(0, stock.getCantidadReservada() - reserva.getCantidadReserva()));
        reserva.setEstadoReserva(EstadoReserva.EXPIRADA);
        stockRepository.save(stock);
        reservaStockRepository.save(reserva);
        revisarAlerta(stock);
    }
}
