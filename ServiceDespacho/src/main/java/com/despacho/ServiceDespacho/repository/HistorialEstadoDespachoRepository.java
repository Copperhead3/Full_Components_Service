package com.despacho.ServiceDespacho.repository;

import com.despacho.ServiceDespacho.model.HistorialEstadoDespacho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialEstadoDespachoRepository extends JpaRepository<HistorialEstadoDespacho, Long> {

    List<HistorialEstadoDespacho> findByDespacho_IdDespachoOrderByFechaAsc(Long idDespacho);
}
