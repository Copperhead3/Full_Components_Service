package com.pago.ServicePago.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pago.ServicePago.model.EstadoReembolso;

public class ReembolsoDto {
    private Long idReembolso;
    private BigDecimal monto;
    private String motivo;
    private LocalDateTime fechaSolicitud;
    private EstadoReembolso estado;

    public ReembolsoDto() {}

    public ReembolsoDto(Long idReembolso, BigDecimal monto, String motivo, LocalDateTime fechaSolicitud,
            EstadoReembolso estado) {
        this.idReembolso = idReembolso;
        this.monto = monto;
        this.motivo = motivo;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
    }

    public Long getIdReembolso() { return idReembolso; }
    public void setIdReembolso(Long idReembolso) { this.idReembolso = idReembolso; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public EstadoReembolso getEstado() { return estado; }
    public void setEstado(EstadoReembolso estado) { this.estado = estado; }
}
