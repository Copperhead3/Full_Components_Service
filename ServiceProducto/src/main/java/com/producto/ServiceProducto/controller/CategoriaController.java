package com.producto.ServiceProducto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.producto.ServiceProducto.model.Categoria;
import com.producto.ServiceProducto.model.DefinicionAtributo;
import com.producto.ServiceProducto.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/categorias")
@Tag(name = "Categorías", description = "Operaciones relacionadas con categorías y definiciones de atributos")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/crear")
    @Operation(summary = "Crear categoría", description = "Registra una nueva categoría de productos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría creada correctamente."),
            @ApiResponse(responseCode = "400", description = "No fue posible crear la categoría.")
    })
    public ResponseEntity<?> postCategoria(@Valid @RequestBody Categoria categoria) {
        try {
            categoriaService.crearCategoria(categoria);
            return ResponseEntity.ok("Categoría creada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Obtiene todas las categorías registradas.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente.")
    public List<Categoria> getCategorias() {
        return categoriaService.listarCategorias();
    }

    @GetMapping("/buscar/{idCategoria}")
    @Operation(summary = "Buscar categoría por ID", description = "Obtiene una categoría mediante su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada."),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada.")
    })
    public ResponseEntity<?> buscarCategoria(@PathVariable Long idCategoria) {
        try {
            return ResponseEntity.ok(categoriaService.buscarPorId(idCategoria));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/modificar/{idCategoria}")
    @Operation(summary = "Modificar categoría", description = "Actualiza el nombre de una categoría existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría modificada correctamente."),
            @ApiResponse(responseCode = "400", description = "No fue posible modificar la categoría.")
    })
    public ResponseEntity<?> putCategoria(@PathVariable Long idCategoria, @Valid @RequestBody Categoria categoria) {
        try {
            categoriaService.modificarCategoria(idCategoria, categoria);
            return ResponseEntity.ok("Categoría modificada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{idCategoria}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría utilizando su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente."),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada.")
    })
    public ResponseEntity<?> deleteCategoria(@PathVariable Long idCategoria) {
        if (categoriaService.eliminarCategoria(idCategoria)) {
            return ResponseEntity.ok("Categoría eliminada correctamente.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoría no encontrada");
    }

    @PostMapping("/{idCategoria}/atributos/crear")
    @Operation(summary = "Crear definición de atributo", description = "Registra una definición de atributo asociada a una categoría.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Definición creada correctamente."),
            @ApiResponse(responseCode = "400", description = "No fue posible crear la definición.")
    })
    public ResponseEntity<?> postDefinicion(
            @PathVariable Long idCategoria,
            @Valid @RequestBody DefinicionAtributo definicion) {
        try {
            categoriaService.crearDefinicion(idCategoria, definicion);
            return ResponseEntity.ok("Definición de atributo creada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idCategoria}/atributos")
    @Operation(summary = "Listar definiciones de atributo", description = "Obtiene las definiciones de atributos de una categoría.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Definiciones obtenidas correctamente."),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada.")
    })
    public ResponseEntity<?> getDefiniciones(@PathVariable Long idCategoria) {
        try {
            return ResponseEntity.ok(categoriaService.listarDefiniciones(idCategoria));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/atributos/modificar/{idDefinicion}")
    @Operation(summary = "Modificar definición de atributo", description = "Actualiza una definición de atributo existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Definición modificada correctamente."),
            @ApiResponse(responseCode = "400", description = "No fue posible modificar la definición.")
    })
    public ResponseEntity<?> putDefinicion(
            @PathVariable Long idDefinicion,
            @Valid @RequestBody DefinicionAtributo definicion) {
        try {
            categoriaService.modificarDefinicion(idDefinicion, definicion);
            return ResponseEntity.ok("Definición modificada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/atributos/eliminar/{idDefinicion}")
    @Operation(summary = "Eliminar definición de atributo", description = "Elimina una definición de atributo existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Definición eliminada correctamente."),
            @ApiResponse(responseCode = "404", description = "Definición no encontrada.")
    })
    public ResponseEntity<?> deleteDefinicion(@PathVariable Long idDefinicion) {
        if (categoriaService.eliminarDefinicion(idDefinicion)) {
            return ResponseEntity.ok("Definición eliminada correctamente.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Definición no encontrada");
    }
}
