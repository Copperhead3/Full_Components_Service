package com.despacho.ServiceDespacho.repository;

import com.despacho.ServiceDespacho.model.Despacho;
import com.despacho.ServiceDespacho.model.EstadoDespacho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DespachoRepository extends JpaRepository<Despacho, Long> {

    Optional<Despacho> findByIdPedido(Long idPedido);

    Optional<Despacho> findByNumeroSeguimiento(String numeroSeguimiento);

    List<Despacho> findByEstado(EstadoDespacho estado);
}
