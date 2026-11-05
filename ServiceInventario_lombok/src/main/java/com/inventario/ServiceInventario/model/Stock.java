package com.inventario.ServiceInventario.model;

import lombok.Getter;
import lombok.Setter;
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
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "stock")
@Getter
@Setter
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStock;

    @NotNull
    @Positive
    @Column(nullable = false, unique = true)
    private Long idProducto;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer cantidadFisica;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer cantidadReservada;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer stockMinimo;

    @Version
    private Long version;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("stock-reservas")
    private List<ReservaStock> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("stock-movimientos")
    private List<MovimientoStock> movimientos = new ArrayList<>();

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("stock-alertas")
    private List<AlertaStock> alertas = new ArrayList<>();

    public Stock() {}

    public Long getIdStock() { return idStock; }
    public void setIdStock(Long idStock) { this.idStock = idStock; }
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public Integer getCantidadFisica() { return cantidadFisica; }
    public void setCantidadFisica(Integer cantidadFisica) { this.cantidadFisica = cantidadFisica; }
    public Integer getCantidadReservada() { return cantidadReservada; }
    public void setCantidadReservada(Integer cantidadReservada) { this.cantidadReservada = cantidadReservada; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public List<ReservaStock> getReservas() { return reservas; }
    public void setReservas(List<ReservaStock> reservas) { this.reservas = reservas; }
    public List<MovimientoStock> getMovimientos() { return movimientos; }
    public void setMovimientos(List<MovimientoStock> movimientos) { this.movimientos = movimientos; }
    public List<AlertaStock> getAlertas() { return alertas; }
    public void setAlertas(List<AlertaStock> alertas) { this.alertas = alertas; }
}
