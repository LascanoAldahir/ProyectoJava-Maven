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
    public ResponseEntity<?> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        // Validaciones de negocio
        if (clienteDTO.getNombre() == null || clienteDTO.getNombre().trim().isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("error", "Nombre requerido", "mensaje", "El nombre del cliente es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (clienteDTO.getClienteId() == null || clienteDTO.getClienteId().trim().isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("error", "Cliente ID requerido", "mensaje", "El ID del cliente es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (clienteDTO.getEdad() < 18) {
            return new ResponseEntity<>(
                    Map.of("error", "Edad inválida", "mensaje", "El cliente debe ser mayor de edad (18+ años)"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (clienteDTO.getContrasena() == null || clienteDTO.getContrasena().length() < 4) {
            return new ResponseEntity<>(
                    Map.of("error", "Contraseña inválida", "mensaje", "La contraseña debe tener al menos 4 caracteres"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            // Verificar si ya existe cliente con ese clienteId
            ClienteDTO existente = clienteService.findByClienteId(clienteDTO.getClienteId());
            if (existente != null) {
                return new ResponseEntity<>(
                        Map.of("error", "Cliente duplicado", "mensaje", "Ya existe un cliente con ID: " + clienteDTO.getClienteId()),
                        HttpStatus.CONFLICT
                );
            }

            ClienteDTO newCliente = clienteService.save(clienteDTO);
            return new ResponseEntity<>(
                    Map.of("mensaje", "Cliente creado exitosamente", "cliente", newCliente),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error al crear cliente", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
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