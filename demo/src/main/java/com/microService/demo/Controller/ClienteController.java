//ClienteController.java
package com.microService.demo.Controller;

import com.microService.demo.dto.ClienteDTO;
import com.microService.demo.services.IClienteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private IClienteServices clienteService;

    @GetMapping
    public ResponseEntity<?> getAllClientes() {
        try {
            List<ClienteDTO> clientes = clienteService.findAll();
            if (clientes.isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("mensaje", "No se encontraron clientes registrados"),
                        HttpStatus.NO_CONTENT
                );
            }
            return new ResponseEntity<>(clientes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno al obtener clientes", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClienteById(@PathVariable Long id) {
        // Validación de ID
        if (id == null || id <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "ID inválido", "mensaje", "El ID debe ser un número positivo"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            ClienteDTO cliente = clienteService.findById(id);
            if (cliente != null) {
                return new ResponseEntity<>(cliente, HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    Map.of("error", "Cliente no encontrado", "mensaje", "No existe cliente con ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody ClienteDTO clienteDTO) {
        // Validar nombre
        if (clienteDTO.getNombre() == null || clienteDTO.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nombre requerido",
                            "mensaje", "El nombre del cliente es obligatorio"));
        }

        if (!clienteDTO.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nombre inválido",
                            "mensaje", "El nombre solo puede contener letras y espacios (2-50 caracteres)"));
        }

        // Validar género
        if (clienteDTO.getGenero() == null || clienteDTO.getGenero().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Género requerido",
                            "mensaje", "El género es obligatorio"));
        }

        if (!clienteDTO.getGenero().toLowerCase().matches("^(masculino|femenino)$")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Género inválido",
                            "mensaje", "El género debe ser: Masculino o Femenino"));
        }

        // Validar edad
        if (clienteDTO.getEdad() == null || clienteDTO.getEdad() < 18 || clienteDTO.getEdad() > 120) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Edad inválida",
                            "mensaje", "La edad debe estar entre 18 y 120 años"));
        }

        // Validar identificación
        if (clienteDTO.getIdentificacion() == null ||
                clienteDTO.getIdentificacion() < 10000000L ||
                clienteDTO.getIdentificacion() > 999999999999999L) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Identificación inválida",
                            "mensaje", "La identificación debe tener entre 8 y 15 dígitos"));
        }
        // Validar dirección
        if (clienteDTO.getDireccion() == null || clienteDTO.getDireccion().trim().length() < 5) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Dirección inválida",
                            "mensaje", "La dirección debe tener al menos 5 caracteres"));
        }
        // Validar teléfono
        if (clienteDTO.getTelefono() == null || !clienteDTO.getTelefono().matches("^\\d{10}$")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Teléfono inválido",
                            "mensaje", "El teléfono debe contener exactamente 10 dígitos"));
        }
        // Validar clienteId - AQUÍ ESTÁ TU PROBLEMA ESPECÍFICO
        if (clienteDTO.getClienteId() == null || clienteDTO.getClienteId().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de cliente requerido",
                            "mensaje", "El ID del cliente es obligatorio"));
        }
        if (!clienteDTO.getClienteId().matches("^[a-zA-Z0-9]{3,20}$")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de cliente inválido",
                            "mensaje", "El ID del cliente solo puede contener letras y números (3-20 caracteres, sin espacios ni caracteres especiales)",
                            "valor_rechazado", clienteDTO.getClienteId(),
                            "caracteres_validos", "Solo letras (a-z, A-Z) y números (0-9)"));
        }
        // Validar contraseña
        if (clienteDTO.getContrasena() == null || clienteDTO.getContrasena().length() < 4) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Contraseña inválida",
                            "mensaje", "La contraseña debe tener al menos 4 caracteres"));
        }

        try {
            // Verificar si ya existe cliente con ese clienteId
            ClienteDTO existente = clienteService.findByClienteId(clienteDTO.getClienteId());
            if (existente != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Cliente duplicado",
                                "mensaje", "Ya existe un cliente con ID: " + clienteDTO.getClienteId()));
            }

            // Normalizar género
            clienteDTO.setGenero(clienteDTO.getGenero());

            ClienteDTO newCliente = clienteService.save(clienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("mensaje", "Cliente creado exitosamente",
                            "cliente", newCliente));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear cliente",
                            "mensaje", e.getMessage()));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "ID inválido", "mensaje", "El ID debe ser un número positivo"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            ClienteDTO updatedCliente = clienteService.update(id, clienteDTO);
            if (updatedCliente != null) {
                return new ResponseEntity<>(
                        Map.of("mensaje", "Cliente actualizado exitosamente", "cliente", updatedCliente),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    Map.of("error", "Cliente no encontrado", "mensaje", "No existe cliente con ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error al actualizar cliente", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "ID inválido", "mensaje", "El ID debe ser un número positivo"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            ClienteDTO cliente = clienteService.findById(id);
            if (cliente == null) {
                return new ResponseEntity<>(
                        Map.of("error", "Cliente no encontrado", "mensaje", "No existe cliente con ID: " + id),
                        HttpStatus.NOT_FOUND
                );
            }

            clienteService.delete(id);
            return new ResponseEntity<>(
                    Map.of("mensaje", "Cliente eliminado exitosamente"),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error al eliminar cliente", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}