package com.producto.ServiceProducto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.producto.ServiceProducto.model.EspecificacionProducto;

@Repository
public interface EspecificacionProductoRepository extends JpaRepository<EspecificacionProducto, Long> {

    List<EspecificacionProducto> findByProducto_IdProducto(Long idProducto);
}
