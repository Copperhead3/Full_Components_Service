package com.producto.ServiceProducto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InformacionDespacho {

    @Column(name = "condiciones_despacho", length = 1000)
    @Size(max = 1000, message = "Las condiciones de despacho no pueden superar 1000 caracteres")
    private String condicionesDespacho;

    @Column(name = "plazo_referencial", length = 150)
    @Size(max = 150, message = "El plazo referencial no puede superar 150 caracteres")
    private String plazoReferencial;

    public String getCondicionesDespacho() {
        return condicionesDespacho;
    }

    public void setCondicionesDespacho(String condicionesDespacho) {
        this.condicionesDespacho = condicionesDespacho;
    }

    public String getPlazoReferencial() {
        return plazoReferencial;
    }

    public void setPlazoReferencial(String plazoReferencial) {
        this.plazoReferencial = plazoReferencial;
    }
}
