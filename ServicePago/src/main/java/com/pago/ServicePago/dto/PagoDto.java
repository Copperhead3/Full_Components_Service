package com.pago.ServicePago.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pago.ServicePago.model.EstadoPago;
import com.pago.ServicePago.model.MetodoPago;

public class PagoDto {
    private Long idPago;
    private Long idPedido;
    private BigDecimal monto;
    private String moneda;
    private LocalDateTime fechaCreacion;
    private EstadoPago estado;
    private MetodoPago metodo;
    private BigDecimal montoReembolsado;
    private BigDecimal montoDisponibleReembolso;

    public PagoDto() {}

    public PagoDto(Long idPago, Long idPedido, BigDecimal monto, String moneda, LocalDateTime fechaCreacion,
            EstadoPago estado, MetodoPago metodo, BigDecimal montoReembolsado, BigDecimal montoDisponibleReembolso) {
        this.idPago = idPago;
        this.idPedido = idPedido;
        this.monto = monto;
        this.moneda = moneda;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.metodo = metodo;
        this.montoReembolsado = montoReembolsado;
        this.montoDisponibleReembolso = montoDisponibleReembolso;
    }

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
    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }
    public MetodoPago getMetodo() { return metodo; }
    public void setMetodo(MetodoPago metodo) { this.metodo = metodo; }
    public BigDecimal getMontoReembolsado() { return montoReembolsado; }
    public void setMontoReembolsado(BigDecimal montoReembolsado) { this.montoReembolsado = montoReembolsado; }
    public BigDecimal getMontoDisponibleReembolso() { return montoDisponibleReembolso; }
    public void setMontoDisponibleReembolso(BigDecimal montoDisponibleReembolso) { this.montoDisponibleReembolso = montoDisponibleReembolso; }
}
