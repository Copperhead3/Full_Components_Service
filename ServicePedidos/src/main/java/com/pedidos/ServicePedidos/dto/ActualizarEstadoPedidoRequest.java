package com.pedidos.ServicePedidos.dto;

import com.pedidos.ServicePedidos.model.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoPedidoRequest {
    @NotNull
    private EstadoPedido estado;

    public ActualizarEstadoPedidoRequest() {}

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
}
