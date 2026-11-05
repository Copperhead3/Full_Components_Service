package com.inventario.ServiceInventario.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "alertas_stock")
@Getter
@Setter
public class AlertaStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlertaStock;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAlerta estadoAlerta;

    @NotNull
    @Column(nullable = false)
    private Integer cantidadDetectada;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacionAlerta;

    private LocalDateTime resueltaEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_stock", nullable = false)
    @JsonBackReference("stock-alertas")
    private Stock stock;

    public AlertaStock() {}

    public Long getIdAlertaStock() { return idAlertaStock; }
    public void setIdAlertaStock(Long idAlertaStock) { this.idAlertaStock = idAlertaStock; }
    public EstadoAlerta getEstadoAlerta() { return estadoAlerta; }
    public void setEstadoAlerta(EstadoAlerta estadoAlerta) { this.estadoAlerta = estadoAlerta; }
    public Integer getCantidadDetectada() { return cantidadDetectada; }
    public void setCantidadDetectada(Integer cantidadDetectada) { this.cantidadDetectada = cantidadDetectada; }
    public LocalDateTime getFechaCreacionAlerta() { return fechaCreacionAlerta; }
    public void setFechaCreacionAlerta(LocalDateTime fechaCreacionAlerta) { this.fechaCreacionAlerta = fechaCreacionAlerta; }
    public LocalDateTime getResueltaEn() { return resueltaEn; }
    public void setResueltaEn(LocalDateTime resueltaEn) { this.resueltaEn = resueltaEn; }
    public Stock getStock() { return stock; }
    public void setStock(Stock stock) { this.stock = stock; }
}
