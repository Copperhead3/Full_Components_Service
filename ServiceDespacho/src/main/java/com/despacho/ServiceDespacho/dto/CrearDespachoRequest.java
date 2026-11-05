package com.despacho.ServiceDespacho.dto;

import com.despacho.ServiceDespacho.model.DireccionEntrega;
import com.despacho.ServiceDespacho.model.ModalidadEntrega;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearDespachoRequest {

    @NotNull
    @Positive
    private Long idPedido;

    @NotNull
    private ModalidadEntrega modalidad;

    @Valid
    private DireccionEntrega direccion;

    public CrearDespachoRequest() {
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public ModalidadEntrega getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadEntrega modalidad) {
        this.modalidad = modalidad;
    }

    public DireccionEntrega getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionEntrega direccion) {
        this.direccion = direccion;
    }
}
