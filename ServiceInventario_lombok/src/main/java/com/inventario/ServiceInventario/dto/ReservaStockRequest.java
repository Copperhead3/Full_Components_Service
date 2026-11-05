package com.inventario.ServiceInventario.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
public class ReservaStockRequest {
    @NotNull @Positive
    private Long idProducto;
    @NotNull @Positive
    private Integer cantidadReserva;
    @NotBlank
    private String referenciaCompra;

    public ReservaStockRequest() {}
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public Integer getCantidadReserva() { return cantidadReserva; }
    public void setCantidadReserva(Integer cantidadReserva) { this.cantidadReserva = cantidadReserva; }
    public String getReferenciaCompra() { return referenciaCompra; }
    public void setReferenciaCompra(String referenciaCompra) { this.referenciaCompra = referenciaCompra; }
}
