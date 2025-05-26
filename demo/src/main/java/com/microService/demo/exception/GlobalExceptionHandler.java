//GlobalExceptionHandler

package com.microService.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        // Si es error de saldo insuficiente, devolver un mensaje específico
        if (ex.getMessage().equals("Saldo no disponible")) {
            body.put("error", "No hay saldo suficiente para realizar esta operación");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        // Para otros errores de ejecución
        body.put("error", "Error de ejecución");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
