package com.carrito.ServiceCarrito.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrito.ServiceCarrito.model.ItemCarrito;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByCarrito_IdCarrito(Long idCarrito);
    Optional<ItemCarrito> findByCarrito_IdCarritoAndIdProducto(Long idCarrito, Long idProducto);
}
