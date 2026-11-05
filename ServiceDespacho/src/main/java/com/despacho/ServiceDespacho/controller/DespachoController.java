package com.despacho.ServiceDespacho.controller;

import com.despacho.ServiceDespacho.dto.ActualizarEstadoDespachoRequest;
import com.despacho.ServiceDespacho.dto.AsignarOperadorRequest;
import com.despacho.ServiceDespacho.dto.CrearDespachoRequest;
import com.despacho.ServiceDespacho.model.Despacho;
import com.despacho.ServiceDespacho.model.EstadoDespacho;
import com.despacho.ServiceDespacho.model.HistorialEstadoDespacho;
import com.despacho.ServiceDespacho.model.OperadorLogistico;
import com.despacho.ServiceDespacho.service.DespachoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/despachos")
@CrossOrigin("*")
@Tag(name = "Despachos", description = "Gestión de despachos y seguimiento de pedidos")
public class DespachoController {

    @Autowired
    private DespachoService despachoService;

    @Operation(summary = "Crear un despacho")
    @PostMapping
    public ResponseEntity<Despacho> crear(
            @Valid @RequestBody CrearDespachoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(despachoService.crearDespacho(request));
    }

    @Operation(summary = "Obtener todos los despachos")
    @GetMapping
    public ResponseEntity<List<Despacho>> obtenerTodos() {
        return ResponseEntity.ok(despachoService.obtenerTodos());
    }

    @Operation(summary = "Obtener despacho por ID")
    @GetMapping("/{idDespacho}")
    public ResponseEntity<Despacho> obtenerPorId(
            @PathVariable Long idDespacho) {

        return ResponseEntity.ok(
                despachoService.obtenerPorId(idDespacho)
        );
    }

    @Operation(summary = "Obtener despacho asociado a un pedido")
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<Despacho> obtenerPorPedido(
            @PathVariable Long idPedido) {

        return ResponseEntity.ok(
                despachoService.obtenerPorPedido(idPedido)
        );
    }

    @Operation(summary = "Consultar despacho por número de seguimiento")
    @GetMapping("/seguimiento/{numeroSeguimiento}")
    public ResponseEntity<Despacho> obtenerPorSeguimiento(
            @PathVariable String numeroSeguimiento) {

        return ResponseEntity.ok(
                despachoService.obtenerPorSeguimiento(numeroSeguimiento)
        );
    }

    @Operation(summary = "Obtener despachos por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Despacho>> obtenerPorEstado(
            @PathVariable EstadoDespacho estado) {

        return ResponseEntity.ok(
                despachoService.obtenerPorEstado(estado)
        );
    }

    @Operation(summary = "Actualizar estado de un despacho")
    @PutMapping("/{idDespacho}/estado")
    public ResponseEntity<Despacho> actualizarEstado(
            @PathVariable Long idDespacho,
            @Valid @RequestBody ActualizarEstadoDespachoRequest request) {

        return ResponseEntity.ok(
                despachoService.actualizarEstado(idDespacho, request)
        );
    }

    @Operation(summary = "Asignar operador logístico a un despacho")
    @PutMapping("/{idDespacho}/operador")
    public ResponseEntity<Despacho> asignarOperador(
            @PathVariable Long idDespacho,
            @Valid @RequestBody AsignarOperadorRequest request) {

        return ResponseEntity.ok(
                despachoService.asignarOperador(idDespacho, request)
        );
    }

    @Operation(summary = "Consultar historial de estados de un despacho")
    @GetMapping("/{idDespacho}/historial")
    public ResponseEntity<List<HistorialEstadoDespacho>> obtenerHistorial(
            @PathVariable Long idDespacho) {

        return ResponseEntity.ok(
                despachoService.obtenerHistorial(idDespacho)
        );
    }

    @Operation(summary = "Registrar operador logístico")
    @PostMapping("/operadores")
    public ResponseEntity<OperadorLogistico> crearOperador(
            @Valid @RequestBody OperadorLogistico operador) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(despachoService.crearOperador(operador));
    }

    @Operation(summary = "Obtener operadores logísticos")
    @GetMapping("/operadores")
    public ResponseEntity<List<OperadorLogistico>> obtenerOperadores() {
        return ResponseEntity.ok(
                despachoService.obtenerOperadores()
        );
    }

    @Operation(summary = "Obtener operador logístico por ID")
    @GetMapping("/operadores/{idOperador}")
    public ResponseEntity<OperadorLogistico> obtenerOperador(
            @PathVariable Long idOperador) {

        return ResponseEntity.ok(
                despachoService.obtenerOperador(idOperador)
        );
    }
}
