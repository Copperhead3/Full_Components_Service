package com.producto.ServiceProducto.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Debe indicar el nombre de la categoría")
    @Size(max = 100, message = "El nombre de la categoría no puede superar 100 caracteres")
    private String nombreCategoria;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria", orphanRemoval = true)
    @JsonManagedReference("categoria-definiciones")
    private List<DefinicionAtributo> definicionesAtributo = new ArrayList<>();

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public List<DefinicionAtributo> getDefinicionesAtributos() {
        return definicionesAtributo;
    }

    public void setDefinicionesAtributos(
            List<DefinicionAtributo> definicionesAtributos) {
        this.definicionesAtributo = definicionesAtributos;
    }
}
