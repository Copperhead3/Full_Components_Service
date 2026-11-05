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
@Table(name = "definiciones_atributo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DefinicionAtributo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDefinicionAtributo;

    @Column(nullable = false, length = 80)
    @NotBlank(message = "Debe indicar la clave del atributo")
    @Size(max = 80, message = "La clave no puede superar 80 caracteres")
    private String clave;

    @Column(nullable = false, length = 120)
    @NotBlank(message = "Debe indicar el nombre del atributo")
    @Size(max = 120, message = "El nombre del atributo no puede superar 120 caracteres")
    private String nombreDefinicionAtributo;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Debe indicar el tipo de dato")
    @Size(max = 50, message = "El tipo de dato no puede superar 50 caracteres")
    private String tipoDato;

    @Column(length = 30)
    @Size(max = 30, message = "La unidad no puede superar 30 caracteres")
    private String unidad;

    @Column(nullable = false)
    @NotNull(message = "Debe indicar si el atributo es filtrable")
    private Boolean filtrable;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    @NotNull(message = "Debe indicar una categoría")
    @JsonBackReference("categoria-definiciones")
    private Categoria categoria;

    public Long getIdDefinicionAtributo() {
        return idDefinicionAtributo;
    }

    public void setIdDefinicionAtributo(Long idDefinicionAtributo) {
        this.idDefinicionAtributo = idDefinicionAtributo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombreAtributo() {
        return nombreDefinicionAtributo;
    }

    public void setNombreAtributo(String nombreAtributo) {
        this.nombreDefinicionAtributo = nombreAtributo;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Boolean getFiltrable() {
        return filtrable;
    }

    public void setFiltrable(Boolean filtrable) {
        this.filtrable = filtrable;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
