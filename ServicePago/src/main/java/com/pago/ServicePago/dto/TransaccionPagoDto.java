package com.pago.ServicePago.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pago.ServicePago.model.EstadoPago;

public class TransaccionPagoDto {
    private Long idTransaccion;
    private String codigoProveedor;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private EstadoPago estado;
    private String respuestaProveedor;

    public TransaccionPagoDto() {}

    public TransaccionPagoDto(Long idTransaccion, String codigoProveedor, BigDecimal monto, LocalDateTime fecha,
            EstadoPago estado, String respuestaProveedor) {
        this.idTransaccion = idTransaccion;
        this.codigoProveedor = codigoProveedor;
        this.monto = monto;
        this.fecha = fecha;
        this.estado = estado;
        this.respuestaProveedor = respuestaProveedor;
    }

    public Long getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(Long idTransaccion) { this.idTransaccion = idTransaccion; }
    public String getCodigoProveedor() { return codigoProveedor; }
    public void setCodigoProveedor(String codigoProveedor) { this.codigoProveedor = codigoProveedor; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }
    public String getRespuestaProveedor() { return respuestaProveedor; }
    public void setRespuestaProveedor(String respuestaProveedor) { this.respuestaProveedor = respuestaProveedor; }
}
