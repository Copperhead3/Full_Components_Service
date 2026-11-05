package com.producto.ServiceProducto.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "especificaciones_producto")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EspecificacionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEspecificacionProducto;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "Debe indicar el valor de la especificación")
    @Size(max = 500, message = "El valor no puede superar 500 caracteres")
    private String valor;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonBackReference("producto-especificaciones")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_definicion_atributo", nullable = false)
    @NotNull(message = "Debe indicar la definición del atributo")
    private DefinicionAtributo definicionAtributo;

    public Long getIdEspecificacionProducto() {
        return idEspecificacionProducto;
    }

    public void setIdEspecificacionProducto(Long idEspecificacionProducto) {
        this.idEspecificacionProducto = idEspecificacionProducto;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public DefinicionAtributo getDefinicionAtributo() {
        return definicionAtributo;
    }

    public void setDefinicionAtributo(
            DefinicionAtributo definicionAtributo) {
        this.definicionAtributo = definicionAtributo;
    }
}
