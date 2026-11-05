package com.inventario.ServiceInventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.ServiceInventario.model.AlertaStock;
import com.inventario.ServiceInventario.model.EstadoAlerta;

public interface AlertaStockRepository extends JpaRepository<AlertaStock, Long> {
    List<AlertaStock> findByEstadoAlerta(EstadoAlerta estadoAlerta);
    Optional<AlertaStock> findFirstByStock_IdStockAndEstadoAlerta(Long idStock, EstadoAlerta estadoAlerta);
}
