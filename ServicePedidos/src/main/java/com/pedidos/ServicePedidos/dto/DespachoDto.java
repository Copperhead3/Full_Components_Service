package com.pedidos.ServicePedidos.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class DespachoDto {
    private Long idDespacho;
    private Long idPedido;
    private LocalDateTime fechaCreacion;
    private String modalidad;
    private String estado;
    private BigDecimal costo;
    private LocalDateTime fechaEstimadaEntrega;
    private String numeroSeguimiento;
    private DireccionEntregaDto direccion;

    public DespachoDto() {}

    public Long getIdDespacho() { return idDespacho; }
    public void setIdDespacho(Long idDespacho) { this.idDespacho = idDespacho; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }
    public LocalDateTime getFechaEstimadaEntrega() { return fechaEstimadaEntrega; }
    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) { this.fechaEstimadaEntrega = fechaEstimadaEntrega; }
    public String getNumeroSeguimiento() { return numeroSeguimiento; }
    public void setNumeroSeguimiento(String numeroSeguimiento) { this.numeroSeguimiento = numeroSeguimiento; }
    public DireccionEntregaDto getDireccion() { return direccion; }
    public void setDireccion(DireccionEntregaDto direccion) { this.direccion = direccion; }
}
