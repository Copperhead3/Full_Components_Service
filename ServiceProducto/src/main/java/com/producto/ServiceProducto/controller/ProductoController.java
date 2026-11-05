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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.producto.ServiceProducto.model.Producto;
import com.producto.ServiceProducto.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con la administración del catálogo de productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/crear")
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto en el catálogo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto creado correctamente."),
            @ApiResponse(responseCode = "400", description = "No fue posible crear el producto.")
    })
    public ResponseEntity<?> postProducto(@Valid @RequestBody Producto producto) {
        try {
            productoService.crearProducto(producto);
            return ResponseEntity.ok("Producto creado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados, independiente de su estado de publicación.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente.")
    public List<Producto> getProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/buscar/{idProducto}")
    @Operation(summary = "Buscar producto por ID", description = "Obtiene un producto utilizando su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<?> buscarProducto(@PathVariable Long idProducto) {
        try {
            return ResponseEntity.ok(productoService.buscarPorId(idProducto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/catalogo")
    @Operation(summary = "Listar catálogo público", description = "Obtiene únicamente los productos que se encuentran publicados.")
    @ApiResponse(responseCode = "200", description = "Catálogo obtenido correctamente.")
    public List<Producto> getCatalogo() {
        return productoService.listarCatalogo();
    }

    @GetMapping("/catalogo/categoria/{idCategoria}")
    @Operation(summary = "Listar catálogo por categoría", description = "Obtiene productos publicados pertenecientes a una categoría.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente."),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada.")
    })
    public ResponseEntity<?> getCatalogoPorCategoria(@PathVariable Long idCategoria) {
        try {
            return ResponseEntity.ok(productoService.listarCatalogoPorCategoria(idCategoria));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos publicados", description = "Busca productos por nombre, marca, modelo o categoría.")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente.")
    public List<Producto> buscarProductos(@RequestParam(required = false) String texto) {
        return productoService.buscarPublicados(texto);
    }

    @PutMapping("/modificar/{idProducto}")
    @Operation(summary = "Modificar producto", description = "Actualiza los datos de un producto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto modificado correctamente."),
            @ApiResponse(responseCode = "400", description = "No fue posible modificar el producto.")
    })
    public ResponseEntity<?> putProducto(@PathVariable Long idProducto, @Valid @RequestBody Producto producto) {
        try {
            productoService.modificarProducto(idProducto, producto);
            return ResponseEntity.ok("Producto modificado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/publicar/{idProducto}")
    @Operation(summary = "Publicar producto", description = "Cambia el estado del producto a PUBLICADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto publicado correctamente."),
            @ApiResponse(responseCode = "400", description = "No fue posible publicar el producto.")
    })
    public ResponseEntity<?> publicarProducto(@PathVariable Long idProducto) {
        try {
            productoService.publicarProducto(idProducto);
            return ResponseEntity.ok("Producto publicado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/despublicar/{idProducto}")
    @Operation(summary = "Despublicar producto", description = "Cambia el estado del producto a DESPUBLICADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto despublicado correctamente."),
            @ApiResponse(responseCode = "400", description = "No fue posible despublicar el producto.")
    })
    public ResponseEntity<?> despublicarProducto(@PathVariable Long idProducto) {
        try {
            productoService.despublicarProducto(idProducto);
            return ResponseEntity.ok("Producto despublicado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/relacionados/{idProducto}")
    @Operation(summary = "Actualizar productos relacionados", description = "Asigna los identificadores de productos relacionados a un producto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos relacionados actualizados correctamente."),
            @ApiResponse(responseCode = "400", description = "No fue posible actualizar los productos relacionados.")
    })
    public ResponseEntity<?> actualizarRelacionados(
            @PathVariable Long idProducto,
            @RequestBody List<Long> idsRelacionados) {
        try {
            productoService.actualizarRelacionados(idProducto, idsRelacionados);
            return ResponseEntity.ok("Productos relacionados actualizados correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{idProducto}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto utilizando su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    public ResponseEntity<?> deleteProducto(@PathVariable Long idProducto) {
        if (productoService.eliminarProducto(idProducto)) {
            return ResponseEntity.ok("Producto eliminado correctamente.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }
}
