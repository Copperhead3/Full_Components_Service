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
@Table(name = "movimientos_stock")
@Getter
@Setter
public class MovimientoStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimientoStock;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @NotNull
    @Column(nullable = false)
    private Integer cantidadMovimiento;

    @NotNull
    @Column(nullable = false)
    private Integer stockAnterior;

    @NotNull
    @Column(nullable = false)
    private Integer stockPosterior;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 120)
    private String referenciaOperacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_stock", nullable = false)
    @JsonBackReference("stock-movimientos")
    private Stock stock;

    public MovimientoStock() {}

    public Long getIdMovimientoStock() { return idMovimientoStock; }
    public void setIdMovimientoStock(Long idMovimientoStock) { this.idMovimientoStock = idMovimientoStock; }
    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }
    public Integer getCantidadMovimiento() { return cantidadMovimiento; }
    public void setCantidadMovimiento(Integer cantidadMovimiento) { this.cantidadMovimiento = cantidadMovimiento; }
    public Integer getStockAnterior() { return stockAnterior; }
    public void setStockAnterior(Integer stockAnterior) { this.stockAnterior = stockAnterior; }
    public Integer getStockPosterior() { return stockPosterior; }
    public void setStockPosterior(Integer stockPosterior) { this.stockPosterior = stockPosterior; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getReferenciaOperacion() { return referenciaOperacion; }
    public void setReferenciaOperacion(String referenciaOperacion) { this.referenciaOperacion = referenciaOperacion; }
    public Stock getStock() { return stock; }
    public void setStock(Stock stock) { this.stock = stock; }
}
