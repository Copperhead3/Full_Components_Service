package com.pago.ServicePago.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pago.ServicePago.dto.CrearPagoRequest;
import com.pago.ServicePago.dto.PagoDto;
import com.pago.ServicePago.dto.ReembolsoDto;
import com.pago.ServicePago.dto.ReembolsoRequest;
import com.pago.ServicePago.dto.TransaccionPagoDto;
import com.pago.ServicePago.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pago", description = "Operaciones de pagos, transacciones y reembolsos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    @Operation(summary = "Crear pago", description = "Crea un pago asociado a un pedido y lo deja en estado PENDIENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago creado correctamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida.")
    })
    public ResponseEntity<PagoDto> crearPago(@Valid @RequestBody CrearPagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crearPago(request));
    }

    @GetMapping("/{idPago}")
    @Operation(summary = "Consultar pago por ID")
    public ResponseEntity<PagoDto> obtenerPago(@PathVariable Long idPago) {
        return ResponseEntity.ok(pagoService.obtenerPago(idPago));
    }

    @GetMapping
    @Operation(summary = "Listar pagos")
    public ResponseEntity<List<PagoDto>> listarPagos() {
        return ResponseEntity.ok(pagoService.listarPagos());
    }

    @GetMapping("/pedido/{idPedido}")
    @Operation(summary = "Consultar pagos de un pedido")
    public ResponseEntity<List<PagoDto>> obtenerPagosPedido(@PathVariable Long idPedido) {
        return ResponseEntity.ok(pagoService.obtenerPagosPedido(idPedido));
    }

    @PostMapping("/{idPago}/iniciar")
    @Operation(summary = "Iniciar pago", description = "Ejecuta una transacción de pago simulada y actualiza el estado del pago.")
    public ResponseEntity<PagoDto> iniciarPago(@PathVariable Long idPago) {
        return ResponseEntity.ok(pagoService.iniciarPago(idPago));
    }

    @PostMapping("/{idPago}/anular")
    @Operation(summary = "Anular pago")
    public ResponseEntity<PagoDto> anularPago(@PathVariable Long idPago) {
        return ResponseEntity.ok(pagoService.anularPago(idPago));
    }

    @GetMapping("/{idPago}/transacciones")
    @Operation(summary = "Consultar transacciones del pago")
    public ResponseEntity<List<TransaccionPagoDto>> obtenerTransacciones(@PathVariable Long idPago) {
        return ResponseEntity.ok(pagoService.obtenerTransacciones(idPago));
    }

    @PostMapping("/{idPago}/reembolsos")
    @Operation(summary = "Solicitar reembolso")
    public ResponseEntity<ReembolsoDto> solicitarReembolso(
            @PathVariable Long idPago,
            @Valid @RequestBody ReembolsoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.solicitarReembolso(idPago, request));
    }

    @GetMapping("/{idPago}/reembolsos")
    @Operation(summary = "Consultar reembolsos del pago")
    public ResponseEntity<List<ReembolsoDto>> obtenerReembolsos(@PathVariable Long idPago) {
        return ResponseEntity.ok(pagoService.obtenerReembolsos(idPago));
    }

    @PostMapping("/{idPago}/reembolsos/{idReembolso}/procesar")
    @Operation(summary = "Procesar reembolso", description = "Procesa una solicitud de reembolso y actualiza el estado del pago.")
    public ResponseEntity<ReembolsoDto> procesarReembolso(
            @PathVariable Long idPago,
            @PathVariable Long idReembolso) {
        return ResponseEntity.ok(pagoService.procesarReembolso(idPago, idReembolso));
    }
}
