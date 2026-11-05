package com.carrito.ServiceCarrito.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrito.ServiceCarrito.model.Carrito;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByIdCliente(Long idCliente);
    boolean existsByIdCliente(Long idCliente);
}
