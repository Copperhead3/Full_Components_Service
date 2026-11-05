package com.pedidos.ServicePedidos.repository;

import com.pedidos.ServicePedidos.model.EstadoPedido;
import com.pedidos.ServicePedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByIdClienteOrderByFechaPedidoDesc(Long idCliente);

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByIdClienteAndEstado(Long idCliente, EstadoPedido estado);
}
