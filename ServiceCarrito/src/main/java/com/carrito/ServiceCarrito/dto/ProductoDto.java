package com.carrito.ServiceCarrito.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductoDto {
    private Long idProducto;
    private String nombreProducto;
    private String marca;
    private String modelo;
    private BigDecimal precioVigente;
    private String estado;
}
