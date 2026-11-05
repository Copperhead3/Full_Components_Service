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
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "reservas_stock")
@Getter
@Setter
public class ReservaStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservaStock;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Long idProducto;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String referenciaCompra;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer cantidadReserva;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estadoReserva;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacionReserva;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime venceEn;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_stock", nullable = false)
    @JsonBackReference("stock-reservas")
    private Stock stock;

    public ReservaStock() {}

    public Long getIdReservaStock() { return idReservaStock; }
    public void setIdReservaStock(Long idReservaStock) { this.idReservaStock = idReservaStock; }
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public String getReferenciaCompra() { return referenciaCompra; }
    public void setReferenciaCompra(String referenciaCompra) { this.referenciaCompra = referenciaCompra; }
    public Integer getCantidadReserva() { return cantidadReserva; }
    public void setCantidadReserva(Integer cantidadReserva) { this.cantidadReserva = cantidadReserva; }
    public EstadoReserva getEstadoReserva() { return estadoReserva; }
    public void setEstadoReserva(EstadoReserva estadoReserva) { this.estadoReserva = estadoReserva; }
    public LocalDateTime getFechaCreacionReserva() { return fechaCreacionReserva; }
    public void setFechaCreacionReserva(LocalDateTime fechaCreacionReserva) { this.fechaCreacionReserva = fechaCreacionReserva; }
    public LocalDateTime getVenceEn() { return venceEn; }
    public void setVenceEn(LocalDateTime venceEn) { this.venceEn = venceEn; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public Stock getStock() { return stock; }
    public void setStock(Stock stock) { this.stock = stock; }
}
