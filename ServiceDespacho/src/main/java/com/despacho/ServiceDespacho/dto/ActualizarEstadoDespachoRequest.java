package com.despacho.ServiceDespacho.dto;

import com.despacho.ServiceDespacho.model.EstadoDespacho;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoDespachoRequest {

    @NotNull
    private EstadoDespacho estado;

    @Size(max = 255)
    private String observacion;

    public ActualizarEstadoDespachoRequest() {
    }

    public EstadoDespacho getEstado() {
        return estado;
    }

    public void setEstado(EstadoDespacho estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
