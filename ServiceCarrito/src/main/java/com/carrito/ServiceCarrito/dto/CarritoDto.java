package com.carrito.ServiceCarrito.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDto {
    private Long idCarrito;
    private Long idCliente;
    private LocalDateTime fechaCreacion;
    private List<ItemCarritoDto> items;
    private BigDecimal subtotal;
}
