//CuentaController

package com.microService.demo.Controller;

import com.microService.demo.dto.CuentaDTO;
import com.microService.demo.services.ICuentaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private ICuentaServices cuentaService;

    @GetMapping
    public ResponseEntity<?> getAllCuentas() {
        try {
            List<CuentaDTO> cuentas = cuentaService.findAll();
            if (cuentas.isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("mensaje", "No se encontraron cuentas registradas"),
                        HttpStatus.NO_CONTENT
                );
            }
            return new ResponseEntity<>(cuentas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno al obtener cuentas", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<?> getCuentaByNumeroCuenta(@PathVariable Long numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "Número de cuenta inválido", "mensaje", "El número de cuenta debe ser positivo"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            CuentaDTO cuenta = cuentaService.findByNumeroCuenta(numeroCuenta);
            if (cuenta != null) {
                return new ResponseEntity<>(cuenta, HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    Map.of("error", "Cuenta no encontrada", "mensaje", "No existe cuenta con número: " + numeroCuenta),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> getCuentasByClienteId(@PathVariable Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "ID de cliente inválido", "mensaje", "El ID del cliente debe ser positivo"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            List<CuentaDTO> cuentas = cuentaService.findByClienteId(clienteId);
            if (cuentas.isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("mensaje", "No se encontraron cuentas para el cliente ID: " + clienteId),
                        HttpStatus.NO_CONTENT
                );
            }
            return new ResponseEntity<>(cuentas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> createCuenta(@RequestBody CuentaDTO cuentaDTO) {
        try {
            // Validar número de cuenta
            if (cuentaDTO.getNumeroCuenta() == null || cuentaDTO.getNumeroCuenta().trim().isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("error", "Número de cuenta requerido", "mensaje", "El número de cuenta es obligatorio"),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Validar que sea numérico y positivo
            Long numeroCuentaLong = null;
            try {
                numeroCuentaLong = Long.parseLong(cuentaDTO.getNumeroCuenta());
                if (numeroCuentaLong <= 0) {
                    return new ResponseEntity<>(
                            Map.of("error", "Número de cuenta inválido", "mensaje", "El número de cuenta debe ser positivo"),
                            HttpStatus.BAD_REQUEST
                    );
                }
            } catch (NumberFormatException e) {
                return new ResponseEntity<>(
                        Map.of("error", "Número de cuenta inválido", "mensaje", "El número de cuenta debe contener solo números"),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Validar tipo de cuenta
            if (cuentaDTO.getTipoCuenta() == null || cuentaDTO.getTipoCuenta().trim().isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("error", "Tipo de cuenta requerido", "mensaje", "Debe especificar el tipo de cuenta"),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Normalizar tipo de cuenta
            cuentaDTO.setTipoCuenta(cuentaDTO.getTipoCuenta());
            if (!cuentaDTO.getTipoCuenta().matches("^(Ahorro|Corriente)$")) {
                return new ResponseEntity<>(
                        Map.of("error", "Tipo de cuenta inválido", "mensaje", "Los tipos válidos son: Ahorro o Corriente"),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Validar saldo inicial
            if (cuentaDTO.getSaldoInicial() == null || cuentaDTO.getSaldoInicial().trim().isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("error", "Saldo inicial requerido", "mensaje", "El saldo inicial es obligatorio"),
                        HttpStatus.BAD_REQUEST
                );
            }

            try {
                double saldoInicial = Double.parseDouble(cuentaDTO.getSaldoInicial());
                if (saldoInicial < 0) {
                    return new ResponseEntity<>(
                            Map.of("error", "Saldo inicial inválido", "mensaje", "El saldo inicial no puede ser negativo"),
                            HttpStatus.BAD_REQUEST
                    );
                }
            } catch (NumberFormatException e) {
                return new ResponseEntity<>(
                        Map.of("error", "Saldo inicial inválido", "mensaje", "El saldo inicial debe ser un número válido"),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Validar cliente ID
            if (cuentaDTO.getClienteId() == null || cuentaDTO.getClienteId().trim().isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("error", "Cliente ID requerido", "mensaje", "Debe especificar el ID del cliente"),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Verificar si ya existe cuenta con ese número
            CuentaDTO existente = cuentaService.findByNumeroCuenta(numeroCuentaLong);
            if (existente != null) {
                return new ResponseEntity<>(
                        Map.of("error", "Cuenta duplicada", "mensaje", "Ya existe una cuenta con número: " + cuentaDTO.getNumeroCuenta()),
                        HttpStatus.CONFLICT
                );
            }

            // Crear cuenta
            CuentaDTO newCuenta = cuentaService.save(cuentaDTO);
            return new ResponseEntity<>(
                    Map.of("mensaje", "Cuenta creada exitosamente", "cuenta", newCuenta),
                    HttpStatus.CREATED
            );

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Cliente no encontrado")) {
                return new ResponseEntity<>(
                        Map.of("error", "Cliente no encontrado", "mensaje", "No existe cliente con ID: " + cuentaDTO.getClienteId()),
                        HttpStatus.NOT_FOUND
                );
            }
            return new ResponseEntity<>(
                    Map.of("error", "Error al crear cuenta", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping("/{numeroCuenta}")
    public ResponseEntity<?> updateCuenta(@PathVariable Long numeroCuenta, @RequestBody CuentaDTO cuentaDTO) {
        if (numeroCuenta == null || numeroCuenta <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "Número de cuenta inválido", "mensaje", "El número de cuenta debe ser positivo"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            CuentaDTO updatedCuenta = cuentaService.update(numeroCuenta, cuentaDTO);
            if (updatedCuenta != null) {
                return new ResponseEntity<>(
                        Map.of("mensaje", "Cuenta actualizada exitosamente", "cuenta", updatedCuenta),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    Map.of("error", "Cuenta no encontrada", "mensaje", "No existe cuenta con número: " + numeroCuenta),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error al actualizar cuenta", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<?> deleteCuenta(@PathVariable Long numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "Número de cuenta inválido", "mensaje", "El número de cuenta debe ser positivo"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            CuentaDTO cuenta = cuentaService.findByNumeroCuenta(numeroCuenta);
            if (cuenta == null) {
                return new ResponseEntity<>(
                        Map.of("error", "Cuenta no encontrada", "mensaje", "No existe cuenta con número: " + numeroCuenta),
                        HttpStatus.NOT_FOUND
                );
            }

            cuentaService.delete(numeroCuenta);
            return new ResponseEntity<>(
                    Map.of("mensaje", "Cuenta eliminada exitosamente"),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error al eliminar cuenta", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}