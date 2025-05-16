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
    public ResponseEntity<MovimientoDTO> createMovimiento(@RequestBody MovimientoDTO movimientoDTO) {
        MovimientoDTO newMovimiento = movimientoService.save(movimientoDTO);
        return new ResponseEntity<>(newMovimiento, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDTO> updateMovimiento(@PathVariable Long id, @RequestBody MovimientoDTO movimientoDTO) {
        MovimientoDTO updatedMovimiento = movimientoService.update(id, movimientoDTO);
        if (updatedMovimiento != null) {
            return new ResponseEntity<>(updatedMovimiento, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimiento(@PathVariable Long id) {
        movimientoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}