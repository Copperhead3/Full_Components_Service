package com.pedidos.ServicePedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearPedidoRequest {

    @NotNull
    @Positive
    private Long idCliente;

    @NotBlank
    private String modalidadEntrega;

    @NotBlank
    private String metodoPago;

    @Valid
    private DireccionEntregaDto direccion;

    public CrearPedidoRequest() {}

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public String getModalidadEntrega() { return modalidadEntrega; }
    public void setModalidadEntrega(String modalidadEntrega) { this.modalidadEntrega = modalidadEntrega; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public DireccionEntregaDto getDireccion() { return direccion; }
    public void setDireccion(DireccionEntregaDto direccion) { this.direccion = direccion; }
}
