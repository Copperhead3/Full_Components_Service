package com.inventario.ServiceInventario.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProductoDto {
    private Long idProducto;
    private String nombreProducto;
    private String estado;

    public ProductoDto() {}
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
