package com.pago.ServicePago.dto;

import java.math.BigDecimal;

import com.pago.ServicePago.model.MetodoPago;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearPagoRequest {

    @NotNull
    @Positive
    private Long idPedido;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal monto;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z]{3}$")
    private String moneda;

    @NotNull
    private MetodoPago metodo;

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public MetodoPago getMetodo() { return metodo; }
    public void setMetodo(MetodoPago metodo) { this.metodo = metodo; }
}
