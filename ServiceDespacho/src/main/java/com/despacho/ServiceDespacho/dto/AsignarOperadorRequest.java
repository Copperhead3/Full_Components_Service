package com.despacho.ServiceDespacho.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignarOperadorRequest {

    @NotNull
    @Positive
    private Long idOperador;

    public AsignarOperadorRequest() {
    }

    public Long getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Long idOperador) {
        this.idOperador = idOperador;
    }
}
