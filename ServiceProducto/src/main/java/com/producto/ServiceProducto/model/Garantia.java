package com.producto.ServiceProducto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
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
public class Garantia {

    @Column(name = "garantia_periodo_meses")
    @Min(value = 0, message = "El período de garantía no puede ser negativo")
    private Integer periodoMeses;

    @Column(name = "garantia_condiciones", length = 1000)
    @Size(max = 1000, message = "Las condiciones de garantía no pueden superar 1000 caracteres")
    private String condicionesGarantia;

    public Integer getPeriodoMeses() {
        return periodoMeses;
    }

    public void setPeriodoMeses(Integer periodoMeses) {
        this.periodoMeses = periodoMeses;
    }

    public String getCondicionesGarantia() {
        return condicionesGarantia;
    }

    public void setCondicionesGarantia(String condicionesGarantia) {
        this.condicionesGarantia = condicionesGarantia;
    }
}
