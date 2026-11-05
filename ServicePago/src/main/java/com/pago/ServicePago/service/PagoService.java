package com.pago.ServicePago.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pago.ServicePago.dto.CrearPagoRequest;
import com.pago.ServicePago.dto.PagoDto;
import com.pago.ServicePago.dto.ReembolsoDto;
import com.pago.ServicePago.dto.ReembolsoRequest;
import com.pago.ServicePago.dto.TransaccionPagoDto;
import com.pago.ServicePago.model.EstadoPago;
import com.pago.ServicePago.model.EstadoReembolso;
import com.pago.ServicePago.model.Pago;
import com.pago.ServicePago.model.Reembolso;
import com.pago.ServicePago.model.TransaccionPago;
import com.pago.ServicePago.repository.PagoRepository;
import com.pago.ServicePago.repository.ReembolsoRepository;
import com.pago.ServicePago.repository.TransaccionPagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private TransaccionPagoRepository transaccionPagoRepository;

    @Autowired
    private ReembolsoRepository reembolsoRepository;

    @Transactional
    public PagoDto crearPago(CrearPagoRequest request) {
        Pago pago = new Pago();
        pago.setIdPedido(request.getIdPedido());
        pago.setMonto(request.getMonto());
        pago.setMoneda(request.getMoneda().toUpperCase());
        pago.setFechaCreacion(LocalDateTime.now());
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setMetodo(request.getMetodo());
        return convertirADto(pagoRepository.save(pago));
    }

    @Transactional(readOnly = true)
    public PagoDto obtenerPago(Long idPago) {
        return convertirADto(buscarPago(idPago));
    }

    @Transactional(readOnly = true)
    public List<PagoDto> obtenerPagosPedido(Long idPedido) {
        return pagoRepository.findByIdPedidoOrderByFechaCreacionDesc(idPedido)
                .stream().map(this::convertirADto).toList();
    }

    @Transactional(readOnly = true)
    public List<PagoDto> listarPagos() {
        return pagoRepository.findAll().stream().map(this::convertirADto).toList();
    }

    @Transactional
    public PagoDto iniciarPago(Long idPago) {
        Pago pago = buscarPago(idPago);
        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new RuntimeException("Solo se puede iniciar un pago en estado PENDIENTE.");
        }

        TransaccionPago transaccion = new TransaccionPago();
        transaccion.setPago(pago);
        transaccion.setCodigoProveedor("SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        transaccion.setMonto(pago.getMonto());
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setEstado(EstadoPago.APROBADO);
        transaccion.setRespuestaProveedor("Pago aprobado por pasarela simulada.");
        transaccionPagoRepository.save(transaccion);

        pago.setEstado(EstadoPago.APROBADO);
        return convertirADto(pagoRepository.save(pago));
    }

    @Transactional
    public PagoDto anularPago(Long idPago) {
        Pago pago = buscarPago(idPago);
        if (pago.getEstado() != EstadoPago.PENDIENTE && pago.getEstado() != EstadoPago.APROBADO) {
            throw new RuntimeException("El pago no puede ser anulado en su estado actual.");
        }

        BigDecimal reembolsado = montoReembolsado(idPago);
        if (reembolsado.compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("No se puede anular un pago que ya posee reembolsos aprobados.");
        }

        pago.setEstado(EstadoPago.ANULADO);
        return convertirADto(pagoRepository.save(pago));
    }

    @Transactional
    public ReembolsoDto solicitarReembolso(Long idPago, ReembolsoRequest request) {
        Pago pago = buscarPago(idPago);
        if (pago.getEstado() != EstadoPago.APROBADO && pago.getEstado() != EstadoPago.REEMBOLSO_PARCIAL) {
            throw new RuntimeException("Solo se pueden reembolsar pagos aprobados.");
        }

        BigDecimal disponible = montoDisponibleReembolso(pago);
        if (request.getMonto().compareTo(disponible) > 0) {
            throw new RuntimeException("El monto solicitado supera el monto disponible para reembolso.");
        }

        Reembolso reembolso = new Reembolso();
        reembolso.setPago(pago);
        reembolso.setMonto(request.getMonto());
        reembolso.setMotivo(request.getMotivo());
        reembolso.setFechaSolicitud(LocalDateTime.now());
        reembolso.setEstado(EstadoReembolso.SOLICITADO);
        return convertirReembolsoADto(reembolsoRepository.save(reembolso));
    }

    @Transactional
    public ReembolsoDto procesarReembolso(Long idPago, Long idReembolso) {
        Pago pago = buscarPago(idPago);
        Reembolso reembolso = reembolsoRepository.findById(idReembolso)
                .orElseThrow(() -> new RuntimeException("Reembolso no encontrado."));

        if (!reembolso.getPago().getIdPago().equals(idPago)) {
            throw new RuntimeException("El reembolso no pertenece al pago indicado.");
        }
        if (reembolso.getEstado() != EstadoReembolso.SOLICITADO) {
            throw new RuntimeException("El reembolso ya fue procesado.");
        }

        reembolso.setEstado(EstadoReembolso.EN_PROCESO);
        reembolsoRepository.save(reembolso);
        reembolso.setEstado(EstadoReembolso.APROBADO);
        Reembolso guardado = reembolsoRepository.save(reembolso);

        BigDecimal totalReembolsado = montoReembolsado(idPago);
        if (totalReembolsado.compareTo(pago.getMonto()) >= 0) {
            pago.setEstado(EstadoPago.REEMBOLSADO);
        } else {
            pago.setEstado(EstadoPago.REEMBOLSO_PARCIAL);
        }
        pagoRepository.save(pago);

        return convertirReembolsoADto(guardado);
    }

    @Transactional(readOnly = true)
    public List<TransaccionPagoDto> obtenerTransacciones(Long idPago) {
        buscarPago(idPago);
        return transaccionPagoRepository.findByPago_IdPagoOrderByFechaDesc(idPago)
                .stream().map(this::convertirTransaccionADto).toList();
    }

    @Transactional(readOnly = true)
    public List<ReembolsoDto> obtenerReembolsos(Long idPago) {
        buscarPago(idPago);
        return reembolsoRepository.findByPago_IdPagoOrderByFechaSolicitudDesc(idPago)
                .stream().map(this::convertirReembolsoADto).toList();
    }

    private Pago buscarPago(Long idPago) {
        return pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado."));
    }

    private BigDecimal montoReembolsado(Long idPago) {
        BigDecimal monto = reembolsoRepository.sumarMontoPorPagoYEstado(idPago, EstadoReembolso.APROBADO);
        return monto == null ? BigDecimal.ZERO : monto;
    }

    private BigDecimal montoDisponibleReembolso(Pago pago) {
        BigDecimal disponible = pago.getMonto().subtract(montoReembolsado(pago.getIdPago()));
        return disponible.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : disponible;
    }

    private PagoDto convertirADto(Pago pago) {
        BigDecimal reembolsado = montoReembolsado(pago.getIdPago());
        BigDecimal disponible = pago.getMonto().subtract(reembolsado);
        if (disponible.compareTo(BigDecimal.ZERO) < 0) {
            disponible = BigDecimal.ZERO;
        }
        return new PagoDto(
                pago.getIdPago(),
                pago.getIdPedido(),
                pago.getMonto(),
                pago.getMoneda(),
                pago.getFechaCreacion(),
                pago.getEstado(),
                pago.getMetodo(),
                reembolsado,
                disponible);
    }

    private TransaccionPagoDto convertirTransaccionADto(TransaccionPago transaccion) {
        return new TransaccionPagoDto(
                transaccion.getIdTransaccion(),
                transaccion.getCodigoProveedor(),
                transaccion.getMonto(),
                transaccion.getFecha(),
                transaccion.getEstado(),
                transaccion.getRespuestaProveedor());
    }

    private ReembolsoDto convertirReembolsoADto(Reembolso reembolso) {
        return new ReembolsoDto(
                reembolso.getIdReembolso(),
                reembolso.getMonto(),
                reembolso.getMotivo(),
                reembolso.getFechaSolicitud(),
                reembolso.getEstado());
    }
}
