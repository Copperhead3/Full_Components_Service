package com.pedidos.ServicePedidos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearDespachoRequest {
    private Long idPedido;
    private String modalidad;
    private DireccionEntregaDto direccion;

    public CrearDespachoRequest() {}

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public DireccionEntregaDto getDireccion() { return direccion; }
    public void setDireccion(DireccionEntregaDto direccion) { this.direccion = direccion; }
}
