package com.pedidos.ServicePedidos.repository;

import com.pedidos.ServicePedidos.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedido_IdPedido(Long idPedido);

    Optional<DetallePedido> findByPedido_IdPedidoAndIdProducto(
            Long idPedido,
            Long idProducto
    );
}
