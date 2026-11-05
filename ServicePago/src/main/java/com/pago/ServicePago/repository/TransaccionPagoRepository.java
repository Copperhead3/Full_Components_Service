package com.pago.ServicePago.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pago.ServicePago.model.TransaccionPago;

@Repository
public interface TransaccionPagoRepository extends JpaRepository<TransaccionPago, Long> {
    List<TransaccionPago> findByPago_IdPagoOrderByFechaDesc(Long idPago);
}
