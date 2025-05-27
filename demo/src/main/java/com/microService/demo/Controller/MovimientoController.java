//MovimientoController

package com.microService.demo.Controller;

import com.microService.demo.dto.MovimientoDTO;
import com.microService.demo.services.IMovimientoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private IMovimientoServices movimientoService;

    @GetMapping
    public ResponseEntity<?> getAllMovimientos() {
        try {
            List<MovimientoDTO> movimientos = movimientoService.findAll();
            if (movimientos.isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("mensaje", "No se encontraron movimientos registrados"),
                        HttpStatus.NO_CONTENT
                );
            }
            return new ResponseEntity<>(movimientos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno al obtener movimientos", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovimientoById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "ID inválido", "mensaje", "El ID debe ser un número positivo"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            MovimientoDTO movimiento = movimientoService.findById(id);
            if (movimiento != null) {
                return new ResponseEntity<>(movimiento, HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    Map.of("error", "Movimiento no encontrado", "mensaje", "No existe movimiento con ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    public ResponseEntity<?> getMovimientosByNumeroCuenta(@PathVariable String numeroCuenta) {
        // Validar formato de número de cuenta
        if (numeroCuenta == null || !numeroCuenta.matches("^\\d{6,12}$")) {
            return new ResponseEntity<>(
                    Map.of("error", "Número de cuenta inválido",
                            "mensaje", "El número de cuenta debe contener entre 6 y 12 dígitos"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            Long numeroCuentaLong = Long.parseLong(numeroCuenta);
            List<MovimientoDTO> movimientos = movimientoService.findByNumeroCuenta(numeroCuentaLong);
            if (movimientos.isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("mensaje", "No se encontraron movimientos para la cuenta: " + numeroCuenta),
                        HttpStatus.NO_CONTENT
                );
            }
            return new ResponseEntity<>(movimientos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/cuenta/{numeroCuenta}/fecha")
    public ResponseEntity<?> getMovimientosByNumeroCuentaAndFechaBetween(
            @PathVariable String numeroCuenta,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        // Validaciones
        if (numeroCuenta == null || !numeroCuenta.matches("^\\d{6,12}$")) {
            return new ResponseEntity<>(
                    Map.of("error", "Número de cuenta inválido",
                            "mensaje", "El número de cuenta debe contener entre 6 y 12 dígitos"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (fechaInicio == null || fechaFin == null) {
            return new ResponseEntity<>(
                    Map.of("error", "Fechas requeridas",
                            "mensaje", "Debe proporcionar fechaInicio y fechaFin en formato YYYY-MM-DD"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (fechaInicio.isAfter(fechaFin)) {
            return new ResponseEntity<>(
                    Map.of("error", "Rango de fechas inválido",
                            "mensaje", "La fecha de inicio no puede ser posterior a la fecha final"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            Long numeroCuentaLong = Long.parseLong(numeroCuenta);
            List<MovimientoDTO> movimientos = movimientoService.findByNumeroCuentaAndFechaBetween(
                    numeroCuentaLong, fechaInicio, fechaFin);

            if (movimientos.isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("mensaje", "No se encontraron movimientos en el rango de fechas especificado"),
                        HttpStatus.NO_CONTENT
                );
            }
            return new ResponseEntity<>(movimientos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> createMovimiento(@RequestBody MovimientoDTO movimientoDTO) {
        try {
            // Validaciones específicas de negocio
            if (movimientoDTO.getFecha() == null) {
                movimientoDTO.setFecha(LocalDate.now());
            }

            // Validar que la fecha no sea futura
            if (movimientoDTO.getFecha().isAfter(LocalDate.now())) {
                return new ResponseEntity<>(
                        Map.of("error", "Fecha inválida",
                                "mensaje", "La fecha del movimiento no puede ser futura"),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Normalizar tipo de movimiento
            movimientoDTO.setTipoMovimiento(movimientoDTO.getTipoMovimiento());

            // Validaciones específicas por tipo
            if ("Deposito".equals(movimientoDTO.getTipoMovimiento())) {
                if (movimientoDTO.getValorAsDouble() <= 0) {
                    return new ResponseEntity<>(
                            Map.of("error", "Valor inválido para depósito",
                                    "mensaje", "Los depósitos deben tener un valor positivo"),
                            HttpStatus.BAD_REQUEST
                    );
                }
            } else if ("Retiro".equals(movimientoDTO.getTipoMovimiento())) {
                if (movimientoDTO.getValorAsDouble() >= 0) {
                    return new ResponseEntity<>(
                            Map.of("error", "Valor inválido para retiro",
                                    "mensaje", "Los retiros deben tener un valor negativo"),
                            HttpStatus.BAD_REQUEST
                    );
                }
            }

            MovimientoDTO newMovimiento = movimientoService.save(movimientoDTO);
            return new ResponseEntity<>(
                    Map.of("mensaje", "Movimiento registrado exitosamente",
                            "movimiento", newMovimiento),
                    HttpStatus.CREATED
            );

        } catch (RuntimeException e) {
            if (e.getMessage().equals("Saldo no disponible")) {
                return new ResponseEntity<>(
                        Map.of("error", "Saldo no disponible",
                                "mensaje", "No hay saldo suficiente para realizar este retiro"),
                        HttpStatus.BAD_REQUEST
                );
            } else if (e.getMessage().contains("Cuenta no encontrada")) {
                return new ResponseEntity<>(
                        Map.of("error", "Cuenta no encontrada",
                                "mensaje", "No existe la cuenta especificada: " + movimientoDTO.getNumeroCuenta()),
                        HttpStatus.NOT_FOUND
                );
            }
            return new ResponseEntity<>(
                    Map.of("error", "Error al crear movimiento", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error interno", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovimiento(@PathVariable Long id, @RequestBody MovimientoDTO movimientoDTO) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "ID inválido", "mensaje", "El ID debe ser un número positivo"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            MovimientoDTO updatedMovimiento = movimientoService.update(id, movimientoDTO);
            if (updatedMovimiento != null) {
                return new ResponseEntity<>(
                        Map.of("mensaje", "Movimiento actualizado exitosamente",
                                "movimiento", updatedMovimiento),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    Map.of("error", "Movimiento no encontrado",
                            "mensaje", "No existe movimiento con ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Saldo no disponible")) {
                return new ResponseEntity<>(
                        Map.of("error", "Saldo no disponible",
                                "mensaje", "La modificación resultaría en saldo insuficiente"),
                        HttpStatus.BAD_REQUEST
                );
            }
            return new ResponseEntity<>(
                    Map.of("error", "Error al actualizar movimiento", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovimiento(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(
                    Map.of("error", "ID inválido", "mensaje", "El ID debe ser un número positivo"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            MovimientoDTO movimiento = movimientoService.findById(id);
            if (movimiento == null) {
                return new ResponseEntity<>(
                        Map.of("error", "Movimiento no encontrado",
                                "mensaje", "No existe movimiento con ID: " + id),
                        HttpStatus.NOT_FOUND
                );
            }

            movimientoService.delete(id);
            return new ResponseEntity<>(
                    Map.of("mensaje", "Movimiento eliminado exitosamente"),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Error al eliminar movimiento", "detalle", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}