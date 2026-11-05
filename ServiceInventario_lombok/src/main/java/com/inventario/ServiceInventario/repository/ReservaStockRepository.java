package com.inventario.ServiceInventario.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.ServiceInventario.model.EstadoReserva;
import com.inventario.ServiceInventario.model.ReservaStock;

public interface ReservaStockRepository extends JpaRepository<ReservaStock, Long> {
    Optional<ReservaStock> findByReferenciaCompra(String referenciaCompra);
    List<ReservaStock> findByEstadoReserva(EstadoReserva estadoReserva);
    List<ReservaStock> findByStock_IdStock(Long idStock);
    List<ReservaStock> findByVenceEnBeforeAndEstadoReserva(LocalDateTime fecha, EstadoReserva estadoReserva);
}
