package com.inventario.ServiceInventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inventario.ServiceInventario.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByIdProducto(Long idProducto);
    boolean existsByIdProducto(Long idProducto);

    @Query("SELECT s FROM Stock s WHERE (s.cantidadFisica - s.cantidadReservada) <= s.stockMinimo")
    List<Stock> findConStockBajo();
}
