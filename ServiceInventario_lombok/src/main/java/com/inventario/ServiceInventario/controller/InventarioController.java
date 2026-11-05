package com.inventario.ServiceInventario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.ServiceInventario.dto.AjusteStockRequest;
import com.inventario.ServiceInventario.dto.ReservaStockRequest;
import com.inventario.ServiceInventario.model.AlertaStock;
import com.inventario.ServiceInventario.model.MovimientoStock;
import com.inventario.ServiceInventario.model.ReservaStock;
import com.inventario.ServiceInventario.model.Stock;
import com.inventario.ServiceInventario.service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/inventario")
@Tag(name = "Inventario", description = "Operaciones de stock, reservas, movimientos y alertas")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @PostMapping("/stock/registrar")
    @Operation(summary = "Registrar stock", description = "Crea el registro de inventario de un producto validándolo previamente en el microservicio Producto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock registrado correctamente."),
            @ApiResponse(responseCode = "400", description = "No fue posible registrar el stock.")
    })
    public ResponseEntity<?> registrarStock(@Valid @RequestBody Stock stock) {
        try { return ResponseEntity.ok(inventarioService.registrarStock(stock)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @GetMapping("/stock")
    @Operation(summary = "Listar stock")
    public List<Stock> listarStock() { return inventarioService.listarStock(); }

    @GetMapping("/stock/{idStock}")
    @Operation(summary = "Buscar stock por ID")
    public ResponseEntity<?> buscarStock(@PathVariable Long idStock) {
        try { return ResponseEntity.ok(inventarioService.buscarStock(idStock)); }
        catch (Exception e) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); }
    }

    @GetMapping("/stock/producto/{idProducto}")
    @Operation(summary = "Consultar stock por producto")
    public ResponseEntity<?> buscarPorProducto(@PathVariable Long idProducto) {
        try { return ResponseEntity.ok(inventarioService.buscarPorProducto(idProducto)); }
        catch (Exception e) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); }
    }

    @GetMapping("/stock/bajo")
    @Operation(summary = "Listar productos con stock bajo")
    public List<Stock> listarStockBajo() { return inventarioService.listarStockBajo(); }

    @PutMapping("/stock/{idStock}/ajustar")
    @Operation(summary = "Ajustar stock físico")
    public ResponseEntity<?> ajustarStock(@PathVariable Long idStock, @Valid @RequestBody AjusteStockRequest request) {
        try { return ResponseEntity.ok(inventarioService.ajustarStock(idStock, request)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @PutMapping("/stock/{idStock}/minimo")
    @Operation(summary = "Cambiar stock mínimo")
    public ResponseEntity<?> cambiarStockMinimo(@PathVariable Long idStock, @RequestParam Integer cantidad) {
        try { return ResponseEntity.ok(inventarioService.cambiarStockMinimo(idStock, cantidad)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @PostMapping("/reservas")
    @Operation(summary = "Reservar stock", description = "Reserva unidades disponibles para una operación de compra.")
    public ResponseEntity<?> reservar(@Valid @RequestBody ReservaStockRequest request) {
        try { return ResponseEntity.ok(inventarioService.reservar(request)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @GetMapping("/reservas/{idReserva}")
    @Operation(summary = "Consultar reserva")
    public ResponseEntity<?> consultarReserva(@PathVariable Long idReserva) {
        try { return ResponseEntity.ok(inventarioService.consultarReserva(idReserva)); }
        catch (Exception e) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); }
    }

    @PostMapping("/reservas/{idReserva}/confirmar")
    @Operation(summary = "Confirmar reserva")
    public ResponseEntity<?> confirmarReserva(@PathVariable Long idReserva) {
        try { return ResponseEntity.ok(inventarioService.confirmarReserva(idReserva)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @PostMapping("/reservas/{idReserva}/liberar")
    @Operation(summary = "Liberar reserva")
    public ResponseEntity<?> liberarReserva(@PathVariable Long idReserva) {
        try { return ResponseEntity.ok(inventarioService.liberarReserva(idReserva)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @PostMapping("/reservas/expirar")
    @Operation(summary = "Expirar reservas vencidas")
    public ResponseEntity<?> expirarReservas() {
        return ResponseEntity.ok("Reservas expiradas: " + inventarioService.expirarReservas());
    }

    @GetMapping("/movimientos/stock/{idStock}")
    @Operation(summary = "Listar movimientos de stock")
    public ResponseEntity<List<MovimientoStock>> listarMovimientos(@PathVariable Long idStock) {
        return ResponseEntity.ok(inventarioService.listarMovimientos(idStock));
    }

    @GetMapping("/alertas")
    @Operation(summary = "Listar alertas")
    public List<AlertaStock> listarAlertas() { return inventarioService.listarAlertas(); }

    @GetMapping("/alertas/activas")
    @Operation(summary = "Listar alertas activas")
    public List<AlertaStock> listarAlertasActivas() { return inventarioService.listarAlertasActivas(); }

    @PutMapping("/alertas/{idAlerta}/resolver")
    @Operation(summary = "Resolver alerta")
    public ResponseEntity<?> resolverAlerta(@PathVariable Long idAlerta) {
        try { return ResponseEntity.ok(inventarioService.resolverAlerta(idAlerta)); }
        catch (Exception e) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); }
    }
}
