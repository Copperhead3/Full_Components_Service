package com.inventario.ServiceInventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.ServiceInventario.model.MovimientoStock;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
    List<MovimientoStock> findByStock_IdStockOrderByFechaDesc(Long idStock);
    List<MovimientoStock> findByReferenciaOperacion(String referenciaOperacion);
}
