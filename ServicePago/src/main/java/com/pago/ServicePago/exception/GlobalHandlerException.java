package com.pago.ServicePago.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, Object> respuesta = base(HttpStatus.BAD_REQUEST);
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        respuesta.put("errores", errores);
        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> manejarRestricciones(ConstraintViolationException ex) {
        Map<String, Object> respuesta = base(HttpStatus.BAD_REQUEST);
        respuesta.put("mensaje", ex.getMessage());
        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarRuntime(RuntimeException ex) {
        Map<String, Object> respuesta = base(HttpStatus.BAD_REQUEST);
        respuesta.put("mensaje", ex.getMessage());
        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarGeneral(Exception ex) {
        Map<String, Object> respuesta = base(HttpStatus.INTERNAL_SERVER_ERROR);
        respuesta.put("mensaje", "Error interno del servidor.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }

    private Map<String, Object> base(HttpStatus estado) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("fecha", LocalDateTime.now());
        respuesta.put("estado", estado.value());
        respuesta.put("error", estado.getReasonPhrase());
        return respuesta;
    }
}
