//GlobalExceptionHandler

package com.microService.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de errores de validación (Respuestas limpias)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error de Validación");

        // Extraer solo los mensajes importantes
        List<String> errores = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.add(error.getDefaultMessage());
        });

        // Si hay un solo error, mostrarlo directamente
        if (errores.size() == 1) {
            body.put("mensaje", errores.get(0));
        } else {
            body.put("errores", errores);
        }

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Manejo de errores de ejecución
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("mensaje", ex.getMessage());

        // Casos específicos
        if (ex.getMessage().equals("Saldo no disponible")) {
            body.put("error", "Saldo Insuficiente");
            body.put("solucion", "Verifique el saldo disponible en su cuenta");
        } else if (ex.getMessage().contains("Cliente no encontrado")) {
            body.put("error", "Cliente No Encontrado");
            body.put("solucion", "Verifique que el ID del cliente sea correcto");
        } else if (ex.getMessage().contains("Cuenta no encontrada")) {
            body.put("error", "Cuenta No Encontrada");
            body.put("solucion", "Verifique que el número de cuenta sea correcto");
        } else {
            body.put("error", "Error de Operación");
        }

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Manejo de errores internos del servidor
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Error Interno del Servidor");
        body.put("mensaje", "Ha ocurrido un error inesperado. Por favor, contacte al administrador.");

        // Solo para desarrollo - remover en producción
        if (System.getProperty("spring.profiles.active", "dev").equals("dev")) {
            body.put("detalle_tecnico", ex.getMessage());
        }

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
