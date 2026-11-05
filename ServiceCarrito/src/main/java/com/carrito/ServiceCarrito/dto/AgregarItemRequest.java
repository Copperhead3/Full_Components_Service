package com.carrito.ServiceCarrito.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgregarItemRequest {
    @NotNull @Positive
    private Long idProducto;

    @NotNull @Min(1)
    private Integer cantidad;
}
