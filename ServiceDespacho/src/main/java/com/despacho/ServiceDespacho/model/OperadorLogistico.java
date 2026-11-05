package com.despacho.ServiceDespacho.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "operadores_logisticos")
@Getter
@Setter
public class OperadorLogistico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOperador;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String nombre;

    @Size(max = 120)
    @Column(length = 120)
    private String empresa;

    @Size(max = 30)
    @Column(length = 30)
    private String telefono;

    @Size(max = 120)
    @Column(length = 120)
    private String email;

    public OperadorLogistico() {
    }

    public Long getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Long idOperador) {
        this.idOperador = idOperador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
