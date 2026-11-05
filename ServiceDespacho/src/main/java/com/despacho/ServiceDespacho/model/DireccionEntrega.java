package com.despacho.ServiceDespacho.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class DireccionEntrega {

    @Size(max = 100)
    private String region;

    @Size(max = 100)
    private String comuna;

    @Size(max = 150)
    private String calle;

    @Size(max = 20)
    private String numero;

    @Size(max = 20)
    private String departamento;

    @Size(max = 255)
    private String referencia;

    public DireccionEntrega() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
