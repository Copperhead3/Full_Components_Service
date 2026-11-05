package com.despacho.ServiceDespacho.service;

import com.despacho.ServiceDespacho.dto.ActualizarEstadoDespachoRequest;
import com.despacho.ServiceDespacho.dto.AsignarOperadorRequest;
import com.despacho.ServiceDespacho.dto.CrearDespachoRequest;
import com.despacho.ServiceDespacho.dto.PedidoDto;
import com.despacho.ServiceDespacho.model.*;
import com.despacho.ServiceDespacho.repository.DespachoRepository;
import com.despacho.ServiceDespacho.repository.HistorialEstadoDespachoRepository;
import com.despacho.ServiceDespacho.repository.OperadorLogisticoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DespachoService {

    @Autowired
    private DespachoRepository despachoRepository;

    @Autowired
    private OperadorLogisticoRepository operadorRepository;

    @Autowired
    private HistorialEstadoDespachoRepository historialRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.pedidos.url}")
    private String pedidosUrl;

    public Despacho crearDespacho(CrearDespachoRequest request) {
        validarPedido(request.getIdPedido());

        if (despachoRepository.findByIdPedido(request.getIdPedido()).isPresent()) {
            throw new RuntimeException("Ya existe un despacho asociado al pedido " + request.getIdPedido());
        }

        if (request.getModalidad() == ModalidadEntrega.DOMICILIO && request.getDireccion() == null) {
            throw new RuntimeException("La dirección es obligatoria para despacho a domicilio");
        }

        Despacho despacho = new Despacho();
        despacho.setIdPedido(request.getIdPedido());
        despacho.setFechaCreacion(LocalDateTime.now());
        despacho.setModalidad(request.getModalidad());
        despacho.setEstado(EstadoDespacho.PENDIENTE);
        despacho.setCosto(calcularCosto(request.getModalidad()));
        despacho.setFechaEstimadaEntrega(calcularFechaEstimada(request.getModalidad()));
        despacho.setNumeroSeguimiento(generarNumeroSeguimiento());
        despacho.setDireccion(
                request.getModalidad() == ModalidadEntrega.DOMICILIO
                        ? request.getDireccion()
                        : null
        );

        Despacho guardado = despachoRepository.save(despacho);
        registrarHistorial(guardado, EstadoDespacho.PENDIENTE, "Despacho creado");

        return guardado;
    }

    public List<Despacho> obtenerTodos() {
        return despachoRepository.findAll();
    }

    public Despacho obtenerPorId(Long idDespacho) {
        return despachoRepository.findById(idDespacho)
                .orElseThrow(() -> new RuntimeException("Despacho no encontrado"));
    }

    public Despacho obtenerPorPedido(Long idPedido) {
        return despachoRepository.findByIdPedido(idPedido)
                .orElseThrow(() -> new RuntimeException("No existe despacho para el pedido indicado"));
    }

    public Despacho obtenerPorSeguimiento(String numeroSeguimiento) {
        return despachoRepository.findByNumeroSeguimiento(numeroSeguimiento)
                .orElseThrow(() -> new RuntimeException("Número de seguimiento no encontrado"));
    }

    public List<Despacho> obtenerPorEstado(EstadoDespacho estado) {
        return despachoRepository.findByEstado(estado);
    }

    public Despacho actualizarEstado(Long idDespacho, ActualizarEstadoDespachoRequest request) {
        Despacho despacho = obtenerPorId(idDespacho);

        validarTransicion(despacho.getEstado(), request.getEstado());

        despacho.setEstado(request.getEstado());
        Despacho guardado = despachoRepository.save(despacho);

        registrarHistorial(
                guardado,
                request.getEstado(),
                request.getObservacion() == null ? "Estado actualizado" : request.getObservacion()
        );

        return guardado;
    }

    public Despacho asignarOperador(Long idDespacho, AsignarOperadorRequest request) {
        Despacho despacho = obtenerPorId(idDespacho);

        OperadorLogistico operador = operadorRepository.findById(request.getIdOperador())
                .orElseThrow(() -> new RuntimeException("Operador logístico no encontrado"));

        despacho.setOperador(operador);
        return despachoRepository.save(despacho);
    }

    public List<HistorialEstadoDespacho> obtenerHistorial(Long idDespacho) {
        obtenerPorId(idDespacho);
        return historialRepository.findByDespacho_IdDespachoOrderByFechaAsc(idDespacho);
    }

    public OperadorLogistico crearOperador(OperadorLogistico operador) {
        operador.setIdOperador(null);
        return operadorRepository.save(operador);
    }

    public List<OperadorLogistico> obtenerOperadores() {
        return operadorRepository.findAll();
    }

    public OperadorLogistico obtenerOperador(Long idOperador) {
        return operadorRepository.findById(idOperador)
                .orElseThrow(() -> new RuntimeException("Operador logístico no encontrado"));
    }

    private void validarPedido(Long idPedido) {
        try {
            PedidoDto pedido = restTemplate.getForObject(
                    pedidosUrl + "/api/v1/pedidos/" + idPedido,
                    PedidoDto.class
            );

            if (pedido == null || pedido.getIdPedido() == null) {
                throw new RuntimeException("No fue posible validar el pedido");
            }

        } catch (RestClientException ex) {
            throw new RuntimeException(
                    "No fue posible validar el pedido " + idPedido + " con el microservicio Pedidos"
            );
        }
    }

    private BigDecimal calcularCosto(ModalidadEntrega modalidad) {
        if (modalidad == ModalidadEntrega.RETIRO_EN_TIENDA) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal("4990");
    }

    private LocalDateTime calcularFechaEstimada(ModalidadEntrega modalidad) {
        if (modalidad == ModalidadEntrega.RETIRO_EN_TIENDA) {
            return LocalDateTime.now().plusDays(1);
        }
        return LocalDateTime.now().plusDays(3);
    }

    private String generarNumeroSeguimiento() {
        return "DSP-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }

    private void registrarHistorial(
            Despacho despacho,
            EstadoDespacho estado,
            String observacion) {

        HistorialEstadoDespacho historial = new HistorialEstadoDespacho();
        historial.setDespacho(despacho);
        historial.setEstado(estado);
        historial.setFecha(LocalDateTime.now());
        historial.setObservacion(observacion);

        historialRepository.save(historial);
    }

    private void validarTransicion(
            EstadoDespacho actual,
            EstadoDespacho nuevo) {

        if (actual == nuevo) {
            throw new RuntimeException("El despacho ya se encuentra en ese estado");
        }

        boolean permitida = switch (actual) {
            case PENDIENTE ->
                    nuevo == EstadoDespacho.PREPARANDO ||
                    nuevo == EstadoDespacho.CANCELADO;

            case PREPARANDO ->
                    nuevo == EstadoDespacho.EN_TRANSITO ||
                    nuevo == EstadoDespacho.CANCELADO;

            case EN_TRANSITO ->
                    nuevo == EstadoDespacho.ENTREGADO;

            case ENTREGADO, CANCELADO -> false;
        };

        if (!permitida) {
            throw new RuntimeException(
                    "Transición de estado no permitida: "
                            + actual + " -> " + nuevo
            );
        }
    }
}
