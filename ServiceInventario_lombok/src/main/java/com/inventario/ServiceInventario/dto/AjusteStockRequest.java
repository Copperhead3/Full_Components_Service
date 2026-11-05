package com.inventario.ServiceInventario.dto;

import lombok.Getter;
import lombok.Setter;
import com.inventario.ServiceInventario.model.TipoMovimiento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
public class AjusteStockRequest {
    @NotNull @Positive
    private Integer cantidad;
    @NotNull
    private TipoMovimiento tipo;
    private String referenciaOperacion;

    public AjusteStockRequest() {}
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }
    public String getReferenciaOperacion() { return referenciaOperacion; }
    public void setReferenciaOperacion(String referenciaOperacion) { this.referenciaOperacion = referenciaOperacion; }
}
