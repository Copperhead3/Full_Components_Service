package com.producto.ServiceProducto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.producto.ServiceProducto.model.ImagenProducto;

@Repository
public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, Long> {

    List<ImagenProducto> findByProducto_IdProductoOrderByOrdenAsc(Long idProducto);
}
