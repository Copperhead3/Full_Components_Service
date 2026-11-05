package com.pago.ServicePago.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pago.ServicePago.model.EstadoPago;
import com.pago.ServicePago.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByIdPedidoOrderByFechaCreacionDesc(Long idPedido);
    List<Pago> findByEstadoOrderByFechaCreacionDesc(EstadoPago estado);
    Optional<Pago> findFirstByIdPedidoOrderByFechaCreacionDesc(Long idPedido);
}
