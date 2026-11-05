package com.carrito.ServiceCarrito.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.carrito.ServiceCarrito.dto.AgregarItemRequest;
import com.carrito.ServiceCarrito.dto.CarritoDto;
import com.carrito.ServiceCarrito.dto.ItemCarritoDto;
import com.carrito.ServiceCarrito.dto.ProductoDto;
import com.carrito.ServiceCarrito.dto.StockDto;
import com.carrito.ServiceCarrito.model.Carrito;
import com.carrito.ServiceCarrito.model.ItemCarrito;
import com.carrito.ServiceCarrito.repository.CarritoRepository;
import com.carrito.ServiceCarrito.repository.ItemCarritoRepository;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.producto.url}")
    private String productoUrl;

    @Value("${services.inventario.url}")
    private String inventarioUrl;

    @Transactional
    public CarritoDto obtenerOCrearCarrito(Long idCliente) {
        Carrito carrito = carritoRepository.findByIdCliente(idCliente).orElseGet(() -> {
            Carrito nuevo = new Carrito();
            nuevo.setIdCliente(idCliente);
            nuevo.setFechaCreacion(LocalDateTime.now());
            return carritoRepository.save(nuevo);
        });
        return convertirADto(carrito, false);
    }

    @Transactional(readOnly = true)
    public CarritoDto obtenerCarrito(Long idCliente) {
        Carrito carrito = buscarCarritoCliente(idCliente);
        return convertirADto(carrito, false);
    }

    @Transactional
    public CarritoDto agregarProducto(Long idCliente, AgregarItemRequest request) {
        ProductoDto producto = obtenerProducto(request.getIdProducto());
        validarProductoPublicado(producto);
        validarStock(request.getIdProducto(), request.getCantidad());

        Carrito carrito = carritoRepository.findByIdCliente(idCliente).orElseGet(() -> {
            Carrito nuevo = new Carrito();
            nuevo.setIdCliente(idCliente);
            nuevo.setFechaCreacion(LocalDateTime.now());
            return carritoRepository.save(nuevo);
        });

        ItemCarrito item = itemCarritoRepository
                .findByCarrito_IdCarritoAndIdProducto(carrito.getIdCarrito(), request.getIdProducto())
                .orElse(null);

        if (item == null) {
            item = new ItemCarrito();
            item.setCarrito(carrito);
            item.setIdProducto(request.getIdProducto());
            item.setCantidad(request.getCantidad());
        } else {
            int nuevaCantidad = item.getCantidad() + request.getCantidad();
            validarStock(request.getIdProducto(), nuevaCantidad);
            item.setCantidad(nuevaCantidad);
        }

        item.setPrecioUnitario(producto.getPrecioVigente());
        itemCarritoRepository.save(item);
        return convertirADto(buscarCarritoCliente(idCliente), false);
    }

    @Transactional
    public CarritoDto actualizarCantidad(Long idCliente, Long idProducto, Integer cantidad) {
        if (cantidad == null || cantidad < 1) {
            throw new RuntimeException("La cantidad debe ser mayor a 0.");
        }

        Carrito carrito = buscarCarritoCliente(idCliente);
        ItemCarrito item = itemCarritoRepository
                .findByCarrito_IdCarritoAndIdProducto(carrito.getIdCarrito(), idProducto)
                .orElseThrow(() -> new RuntimeException("El producto no se encuentra en el carrito."));

        ProductoDto producto = obtenerProducto(idProducto);
        validarProductoPublicado(producto);
        validarStock(idProducto, cantidad);

        item.setCantidad(cantidad);
        item.setPrecioUnitario(producto.getPrecioVigente());
        itemCarritoRepository.save(item);
        return convertirADto(carrito, false);
    }

    @Transactional
    public CarritoDto eliminarProducto(Long idCliente, Long idProducto) {
        Carrito carrito = buscarCarritoCliente(idCliente);
        ItemCarrito item = itemCarritoRepository
                .findByCarrito_IdCarritoAndIdProducto(carrito.getIdCarrito(), idProducto)
                .orElseThrow(() -> new RuntimeException("El producto no se encuentra en el carrito."));
        itemCarritoRepository.delete(item);
        return convertirADto(carrito, false);
    }

    @Transactional
    public CarritoDto vaciarCarrito(Long idCliente) {
        Carrito carrito = buscarCarritoCliente(idCliente);
        List<ItemCarrito> items = itemCarritoRepository.findByCarrito_IdCarrito(carrito.getIdCarrito());
        itemCarritoRepository.deleteAll(items);
        return convertirADto(carrito, false);
    }

    @Transactional
    public CarritoDto validarPrecios(Long idCliente) {
        Carrito carrito = buscarCarritoCliente(idCliente);
        List<ItemCarrito> items = itemCarritoRepository.findByCarrito_IdCarrito(carrito.getIdCarrito());

        for (ItemCarrito item : items) {
            ProductoDto producto = obtenerProducto(item.getIdProducto());
            validarProductoPublicado(producto);
            item.setPrecioUnitario(producto.getPrecioVigente());
            itemCarritoRepository.save(item);
        }

        return convertirADto(carrito, false);
    }

    @Transactional(readOnly = true)
    public Boolean validarCheckout(Long idCliente) {
        Carrito carrito = buscarCarritoCliente(idCliente);
        List<ItemCarrito> items = itemCarritoRepository.findByCarrito_IdCarrito(carrito.getIdCarrito());
        if (items.isEmpty()) {
            return false;
        }

        for (ItemCarrito item : items) {
            ProductoDto producto = obtenerProducto(item.getIdProducto());
            validarProductoPublicado(producto);
            validarStock(item.getIdProducto(), item.getCantidad());
            if (producto.getPrecioVigente() == null || producto.getPrecioVigente().compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        }
        return true;
    }

    private Carrito buscarCarritoCliente(Long idCliente) {
        return carritoRepository.findByIdCliente(idCliente)
                .orElseThrow(() -> new RuntimeException("No existe un carrito para el cliente indicado."));
    }

    private ProductoDto obtenerProducto(Long idProducto) {
        String url = productoUrl + "/api/v1/productos/buscar/" + idProducto;
        ResponseEntity<ProductoDto> respuesta = restTemplate.getForEntity(url, ProductoDto.class);
        if (!respuesta.getStatusCode().is2xxSuccessful() || respuesta.getBody() == null) {
            throw new RuntimeException("El producto indicado no existe.");
        }
        return respuesta.getBody();
    }

    private void validarProductoPublicado(ProductoDto producto) {
        if (producto.getEstado() != null && !"PUBLICADO".equalsIgnoreCase(producto.getEstado())) {
            throw new RuntimeException("El producto no se encuentra publicado.");
        }
        if (producto.getPrecioVigente() == null) {
            throw new RuntimeException("El producto no posee un precio vigente.");
        }
    }

    private void validarStock(Long idProducto, Integer cantidadSolicitada) {
        String url = inventarioUrl + "/api/v1/inventario/stock/producto/" + idProducto;
        ResponseEntity<StockDto> respuesta = restTemplate.getForEntity(url, StockDto.class);
        if (!respuesta.getStatusCode().is2xxSuccessful() || respuesta.getBody() == null) {
            throw new RuntimeException("No existe inventario para el producto indicado.");
        }

        StockDto stock = respuesta.getBody();
        int fisica = stock.getCantidadFisica() == null ? 0 : stock.getCantidadFisica();
        int reservada = stock.getCantidadReservada() == null ? 0 : stock.getCantidadReservada();
        int disponible = fisica - reservada;

        if (cantidadSolicitada > disponible) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + disponible + ".");
        }
    }

    private CarritoDto convertirADto(Carrito carrito, boolean actualizarPrecios) {
        List<ItemCarrito> items = itemCarritoRepository.findByCarrito_IdCarrito(carrito.getIdCarrito());
        List<ItemCarritoDto> itemDtos = new ArrayList<>();
        BigDecimal subtotalCarrito = BigDecimal.ZERO;

        for (ItemCarrito item : items) {
            ProductoDto producto = null;
            String nombre = null;
            try {
                producto = obtenerProducto(item.getIdProducto());
                nombre = producto.getNombreProducto();
                if (actualizarPrecios && producto.getPrecioVigente() != null) {
                    item.setPrecioUnitario(producto.getPrecioVigente());
                }
            } catch (Exception ignored) {
                nombre = "Producto " + item.getIdProducto();
            }

            BigDecimal precio = item.getPrecioUnitario() == null ? BigDecimal.ZERO : item.getPrecioUnitario();
            BigDecimal subtotalItem = precio.multiply(BigDecimal.valueOf(item.getCantidad()));
            subtotalCarrito = subtotalCarrito.add(subtotalItem);

            itemDtos.add(new ItemCarritoDto(
                    item.getIdItem(),
                    item.getIdProducto(),
                    nombre,
                    item.getCantidad(),
                    precio,
                    subtotalItem));
        }

        return new CarritoDto(
                carrito.getIdCarrito(),
                carrito.getIdCliente(),
                carrito.getFechaCreacion(),
                itemDtos,
                subtotalCarrito);
    }
}
