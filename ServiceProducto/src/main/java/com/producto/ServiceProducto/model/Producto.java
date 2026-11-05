package com.producto.ServiceProducto.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "productos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Debe indicar el nombre del producto")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
    private String nombreProducto;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Debe indicar la marca")
    @Size(max = 100, message = "La marca no puede superar 100 caracteres")
    private String marca;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Debe indicar el modelo")
    @Size(max = 100, message = "El modelo no puede superar 100 caracteres")
    private String modelo;

    @Column(nullable = false, length = 2000)
    @NotBlank(message = "Debe indicar una descripción")
    @Size(max = 2000, message = "La descripción no puede superar 2000 caracteres")
    private String descripcion;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Debe indicar un precio vigente")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private BigDecimal precioVigente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @NotNull(message = "Debe indicar un estado de publicación")
    private EstadoPublicacion estado = EstadoPublicacion.BORRADOR;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    @NotNull(message = "Debe indicar una categoría")
    @JsonIgnoreProperties("definicionesAtributo")
    private Categoria categoria;

    @Valid
    @Embedded
    private Garantia garantia;

    @Valid
    @Embedded
    private InformacionDespacho informacionDespacho;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto", orphanRemoval = true)
    @JsonManagedReference("producto-imagenes")
    private List<ImagenProducto> imagenes = new ArrayList<>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto", orphanRemoval = true)
    @JsonManagedReference("producto-especificaciones")
    private List<EspecificacionProducto> especificaciones = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "productos_relacionados", joinColumns = @JoinColumn(name = "id_producto"), inverseJoinColumns = @JoinColumn(name = "id_producto_relacionado"))
    @JsonIgnoreProperties({ "relacionados", "imagenes", "especificaciones" })
    private Set<Producto> relacionados = new LinkedHashSet<>();

    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioVigente() {
        return precioVigente;
    }

    public void setPrecioVigente(BigDecimal precioVigente) {
        this.precioVigente = precioVigente;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPublicacion estado) {
        this.estado = estado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Garantia getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantia garantia) {
        this.garantia = garantia;
    }

    public InformacionDespacho getInformacionDespacho() {
        return informacionDespacho;
    }

    public void setInformacionDespacho(
            InformacionDespacho informacionDespacho) {
        this.informacionDespacho = informacionDespacho;
    }

    public List<ImagenProducto> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenProducto> imagenes) {
        this.imagenes = imagenes;
    }

    public List<EspecificacionProducto> getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(
            List<EspecificacionProducto> especificaciones) {
        this.especificaciones = especificaciones;
    }

    public Set<Producto> getRelacionados() {
        return relacionados;
    }

    public void setRelacionados(Set<Producto> relacionados) {
        this.relacionados = relacionados;
    }
}
