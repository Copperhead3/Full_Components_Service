package com.pedidos.ServicePedidos.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReservaStockDto {
    private Long idReservaStock;
    private Long idProducto;
    private String referenciaCompra;
    private Integer cantidadReserva;
    private String estadoReserva;
    private LocalDateTime fechaCreacionReserva;
    private LocalDateTime venceEn;

    public ReservaStockDto() {}

    public Long getIdReservaStock() { return idReservaStock; }
    public void setIdReservaStock(Long idReservaStock) { this.idReservaStock = idReservaStock; }
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public String getReferenciaCompra() { return referenciaCompra; }
    public void setReferenciaCompra(String referenciaCompra) { this.referenciaCompra = referenciaCompra; }
    public Integer getCantidadReserva() { return cantidadReserva; }
    public void setCantidadReserva(Integer cantidadReserva) { this.cantidadReserva = cantidadReserva; }
    public String getEstadoReserva() { return estadoReserva; }
    public void setEstadoReserva(String estadoReserva) { this.estadoReserva = estadoReserva; }
    public LocalDateTime getFechaCreacionReserva() { return fechaCreacionReserva; }
    public void setFechaCreacionReserva(LocalDateTime fechaCreacionReserva) { this.fechaCreacionReserva = fechaCreacionReserva; }
    public LocalDateTime getVenceEn() { return venceEn; }
    public void setVenceEn(LocalDateTime venceEn) { this.venceEn = venceEn; }
}
