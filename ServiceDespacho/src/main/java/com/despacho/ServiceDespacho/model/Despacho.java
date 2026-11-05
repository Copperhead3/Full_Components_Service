package com.despacho.ServiceDespacho.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "despachos")
@Getter
@Setter
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDespacho;

    @NotNull
    @Positive
    @Column(nullable = false, unique = true)
    private Long idPedido;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ModalidadEntrega modalidad;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoDespacho estado;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal costo;

    private LocalDateTime fechaEstimadaEntrega;

    @Size(max = 100)
    @Column(length = 100, unique = true)
    private String numeroSeguimiento;

    @Valid
    @Embedded
    private DireccionEntrega direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operador")
    private OperadorLogistico operador;

    @OneToMany(mappedBy = "despacho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoDespacho> historial = new ArrayList<>();

    public Despacho() {
    }

    public Long getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(Long idDespacho) {
        this.idDespacho = idDespacho;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ModalidadEntrega getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadEntrega modalidad) {
        this.modalidad = modalidad;
    }

    public EstadoDespacho getEstado() {
        return estado;
    }

    public void setEstado(EstadoDespacho estado) {
        this.estado = estado;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public LocalDateTime getFechaEstimadaEntrega() {
        return fechaEstimadaEntrega;
    }

    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) {
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
    }

    public String getNumeroSeguimiento() {
        return numeroSeguimiento;
    }

    public void setNumeroSeguimiento(String numeroSeguimiento) {
        this.numeroSeguimiento = numeroSeguimiento;
    }

    public DireccionEntrega getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionEntrega direccion) {
        this.direccion = direccion;
    }

    public OperadorLogistico getOperador() {
        return operador;
    }

    public void setOperador(OperadorLogistico operador) {
        this.operador = operador;
    }

    public List<HistorialEstadoDespacho> getHistorial() {
        return historial;
    }

    public void setHistorial(List<HistorialEstadoDespacho> historial) {
        this.historial = historial;
    }
}
