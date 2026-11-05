package com.pedidos.ServicePedidos.controller;

import com.pedidos.ServicePedidos.dto.ActualizarEstadoPedidoRequest;
import com.pedidos.ServicePedidos.dto.CrearPedidoRequest;
import com.pedidos.ServicePedidos.model.EstadoPedido;
import com.pedidos.ServicePedidos.model.Pedido;
import com.pedidos.ServicePedidos.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin("*")
@Tag(name = "Pedidos", description = "Gestión y procesamiento de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Crear y procesar un pedido")
    @PostMapping
    public ResponseEntity<Pedido> crear(
            @Valid @RequestBody CrearPedidoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoService.crearPedido(request));
    }

    @Operation(summary = "Obtener todos los pedidos")
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(
                pedidoService.obtenerTodos()
        );
    }

    @Operation(summary = "Obtener pedido por ID")
    @GetMapping("/{idPedido}")
    public ResponseEntity<Pedido> obtenerPorId(
            @PathVariable Long idPedido) {

        return ResponseEntity.ok(
                pedidoService.obtenerPorId(idPedido)
        );
    }

    @Operation(summary = "Obtener pedidos de un cliente")
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Pedido>> obtenerPorCliente(
            @PathVariable Long idCliente) {

        return ResponseEntity.ok(
                pedidoService.obtenerPorCliente(idCliente)
        );
    }

    @Operation(summary = "Obtener pedidos por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> obtenerPorEstado(
            @PathVariable EstadoPedido estado) {

        return ResponseEntity.ok(
                pedidoService.obtenerPorEstado(estado)
        );
    }

    @Operation(summary = "Actualizar estado de un pedido")
    @PutMapping("/{idPedido}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long idPedido,
            @Valid @RequestBody ActualizarEstadoPedidoRequest request) {

        return ResponseEntity.ok(
                pedidoService.actualizarEstado(
                        idPedido,
                        request.getEstado()
                )
        );
    }

    @Operation(summary = "Cancelar un pedido")
    @PutMapping("/{idPedido}/cancelar")
    public ResponseEntity<Pedido> cancelar(
            @PathVariable Long idPedido) {

        return ResponseEntity.ok(
                pedidoService.cancelarPedido(idPedido)
        );
    }
}
