package com.carrito.ServiceCarrito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrito.ServiceCarrito.dto.ActualizarCantidadRequest;
import com.carrito.ServiceCarrito.dto.AgregarItemRequest;
import com.carrito.ServiceCarrito.dto.CarritoDto;
import com.carrito.ServiceCarrito.service.CarritoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/carritos")
@Tag(name = "Carrito", description = "Operaciones del carrito de compras")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PostMapping("/cliente/{idCliente}")
    @Operation(summary = "Crear u obtener carrito", description = "Obtiene el carrito del cliente o crea uno nuevo si todavía no existe.")
    @ApiResponse(responseCode = "200", description = "Carrito obtenido correctamente.")
    public ResponseEntity<CarritoDto> obtenerOCrear(@PathVariable Long idCliente) {
        return ResponseEntity.ok(carritoService.obtenerOCrearCarrito(idCliente));
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Consultar carrito", description = "Obtiene el contenido actual del carrito de un cliente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito encontrado."),
            @ApiResponse(responseCode = "400", description = "Carrito no encontrado.")
    })
    public ResponseEntity<CarritoDto> obtener(@PathVariable Long idCliente) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(idCliente));
    }

    @PostMapping("/cliente/{idCliente}/items")
    @Operation(summary = "Agregar producto", description = "Valida Producto e Inventario mediante RestTemplate antes de agregar el ítem.")
    public ResponseEntity<CarritoDto> agregarProducto(
            @PathVariable Long idCliente,
            @Valid @RequestBody AgregarItemRequest request) {
        return ResponseEntity.ok(carritoService.agregarProducto(idCliente, request));
    }

    @PutMapping("/cliente/{idCliente}/items/{idProducto}")
    @Operation(summary = "Modificar cantidad", description = "Actualiza la cantidad de un producto y vuelve a validar el stock disponible.")
    public ResponseEntity<CarritoDto> actualizarCantidad(
            @PathVariable Long idCliente,
            @PathVariable Long idProducto,
            @Valid @RequestBody ActualizarCantidadRequest request) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(idCliente, idProducto, request.getCantidad()));
    }

    @DeleteMapping("/cliente/{idCliente}/items/{idProducto}")
    @Operation(summary = "Eliminar producto del carrito")
    public ResponseEntity<CarritoDto> eliminarProducto(
            @PathVariable Long idCliente,
            @PathVariable Long idProducto) {
        return ResponseEntity.ok(carritoService.eliminarProducto(idCliente, idProducto));
    }

    @DeleteMapping("/cliente/{idCliente}/items")
    @Operation(summary = "Vaciar carrito")
    public ResponseEntity<CarritoDto> vaciarCarrito(@PathVariable Long idCliente) {
        return ResponseEntity.ok(carritoService.vaciarCarrito(idCliente));
    }

    @PutMapping("/cliente/{idCliente}/validar-precios")
    @Operation(summary = "Validar precios", description = "Consulta Producto y actualiza cada precio unitario del carrito con el precio vigente.")
    public ResponseEntity<CarritoDto> validarPrecios(@PathVariable Long idCliente) {
        return ResponseEntity.ok(carritoService.validarPrecios(idCliente));
    }

    @GetMapping("/cliente/{idCliente}/validar-checkout")
    @Operation(summary = "Validar carrito para checkout", description = "Comprueba que el carrito tenga productos publicados, precios válidos y stock disponible.")
    public ResponseEntity<Boolean> validarCheckout(@PathVariable Long idCliente) {
        return ResponseEntity.ok(carritoService.validarCheckout(idCliente));
    }
}
