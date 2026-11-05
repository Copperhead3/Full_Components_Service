package com.producto.ServiceProducto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.producto.ServiceProducto.model.EstadoPublicacion;
import com.producto.ServiceProducto.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByMarcaIgnoreCaseAndModeloIgnoreCase(String marca, String modelo);

    List<Producto> findByEstadoOrderByNombreProductoAsc(EstadoPublicacion estado);

    List<Producto> findByCategoria_IdCategoriaAndEstadoOrderByNombreProductoAsc(
            Long idCategoria,
            EstadoPublicacion estado);

    @Query("""
            SELECT p FROM Producto p
            WHERE p.estado = :estado
              AND (
                   LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :texto, '%'))
                OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :texto, '%'))
                OR LOWER(p.modelo) LIKE LOWER(CONCAT('%', :texto, '%'))
                OR LOWER(p.categoria.nombreCategoria) LIKE LOWER(CONCAT('%', :texto, '%'))
              )
            ORDER BY p.nombreProducto ASC
            """)
    List<Producto> buscarPublicadosPorTexto(
            @Param("texto") String texto,
            @Param("estado") EstadoPublicacion estado);
}
