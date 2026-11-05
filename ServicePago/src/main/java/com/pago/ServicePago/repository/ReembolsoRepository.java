package com.pago.ServicePago.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pago.ServicePago.model.EstadoReembolso;
import com.pago.ServicePago.model.Reembolso;

@Repository
public interface ReembolsoRepository extends JpaRepository<Reembolso, Long> {
    List<Reembolso> findByPago_IdPagoOrderByFechaSolicitudDesc(Long idPago);

    @Query("SELECT COALESCE(SUM(r.monto), 0) FROM Reembolso r WHERE r.pago.idPago = :idPago AND r.estado = :estado")
    BigDecimal sumarMontoPorPagoYEstado(@Param("idPago") Long idPago, @Param("estado") EstadoReembolso estado);
}
