package com.pedidos.ServicePedidos.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PagoDto {
    private Long idPago;
    private Long idPedido;
    private BigDecimal monto;
    private String moneda;
    private LocalDateTime fechaCreacion;
    private String estado;
    private String metodo;
    private BigDecimal montoReembolsado;
    private BigDecimal montoDisponibleReembolso;

    public PagoDto() {}

    public Long getIdPago() { return idPago; }
    public void setIdPago(Long idPago) { this.idPago = idPago; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    public BigDecimal getMontoReembolsado() { return montoReembolsado; }
    public void setMontoReembolsado(BigDecimal montoReembolsado) { this.montoReembolsado = montoReembolsado; }
    public BigDecimal getMontoDisponibleReembolso() { return montoDisponibleReembolso; }
    public void setMontoDisponibleReembolso(BigDecimal montoDisponibleReembolso) { this.montoDisponibleReembolso = montoDisponibleReembolso; }
}
