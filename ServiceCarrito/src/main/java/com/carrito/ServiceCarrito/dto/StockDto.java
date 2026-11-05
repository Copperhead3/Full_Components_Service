package com.carrito.ServiceCarrito.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockDto {
    private Long idStock;
    private Long idProducto;
    private Integer cantidadFisica;
    private Integer cantidadReservada;
    private Integer stockMinimo;
    private Long version;
}
