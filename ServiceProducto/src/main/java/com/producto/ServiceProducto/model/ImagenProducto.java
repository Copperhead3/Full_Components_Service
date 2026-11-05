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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "imagenes_producto")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImagenProducto;

    @Column(nullable = false, length = 1000)
    @NotBlank(message = "Debe indicar la URL de la imagen")
    @Size(max = 1000, message = "La URL no puede superar 1000 caracteres")
    private String url;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Debe indicar el formato de la imagen")
    @Size(max = 20, message = "El formato no puede superar 20 caracteres")
    private String formato;

    @Column(nullable = false)
    @NotNull(message = "Debe indicar el orden de la imagen")
    @Min(value = 0, message = "El orden no puede ser negativo")
    private Integer orden;

    @Column(nullable = false)
    @NotNull(message = "Debe indicar si es la imagen principal")
    private Boolean esPrincipal;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonBackReference("producto-imagenes")
    private Producto producto;

    public Long getIdImagenProducto() {
        return idImagenProducto;
    }

    public void setIdImagenProducto(Long idImagenProducto) {
        this.idImagenProducto = idImagenProducto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(Boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
