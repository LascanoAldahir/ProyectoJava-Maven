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
    public ResponseEntity<List<MovimientoDTO>> getAllMovimientos() {
        List<MovimientoDTO> movimientos = movimientoService.findAll();
        return new ResponseEntity<>(movimientos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> getMovimientoById(@PathVariable Long id) {
        MovimientoDTO movimiento = movimientoService.findById(id);
        if (movimiento != null) {
            return new ResponseEntity<>(movimiento, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    public ResponseEntity<List<MovimientoDTO>> getMovimientosByNumeroCuenta(@PathVariable Long numeroCuenta) {
        List<MovimientoDTO> movimientos = movimientoService.findByNumeroCuenta(numeroCuenta);
        return new ResponseEntity<>(movimientos, HttpStatus.OK);
    }

    @GetMapping("/cuenta/{numeroCuenta}/fecha")
    public ResponseEntity<List<MovimientoDTO>> getMovimientosByNumeroCuentaAndFechaBetween(
            @PathVariable Long numeroCuenta,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<MovimientoDTO> movimientos = movimientoService.findByNumeroCuentaAndFechaBetween(numeroCuenta, fechaInicio, fechaFin);
        return new ResponseEntity<>(movimientos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createMovimiento(@RequestBody MovimientoDTO movimientoDTO) {
        try {
            MovimientoDTO newMovimiento = movimientoService.save(movimientoDTO);
            return new ResponseEntity<>(newMovimiento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Si la excepción es por saldo insuficiente, devolver un mensaje específico
            if (e.getMessage().equals("Saldo no disponible")) {
                return new ResponseEntity<>(Map.of("error", "Saldo no disponible", "mensaje", e.getMessage()),
                        HttpStatus.BAD_REQUEST);
            }
            // Para otros errores
            return new ResponseEntity<>(Map.of("error", "Error al crear movimiento", "mensaje", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovimiento(@PathVariable Long id, @RequestBody MovimientoDTO movimientoDTO) {
        try {
            MovimientoDTO updatedMovimiento = movimientoService.update(id, movimientoDTO);
            if (updatedMovimiento != null) {
                return new ResponseEntity<>(updatedMovimiento, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            // Si la excepción es por saldo insuficiente, devolver un mensaje específico
            if (e.getMessage().equals("Saldo no disponible")) {
                return new ResponseEntity<>(Map.of("error", "Saldo no disponible", "mensaje", e.getMessage()),
                        HttpStatus.BAD_REQUEST);
            }
            // Para otros errores
            return new ResponseEntity<>(Map.of("error", "Error al actualizar movimiento", "mensaje", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovimiento(@PathVariable Long id) {
        try {
            movimientoService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", "Error al eliminar movimiento", "mensaje", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}