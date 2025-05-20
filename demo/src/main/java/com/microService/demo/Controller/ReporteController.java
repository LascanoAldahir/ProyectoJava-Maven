package com.microService.demo.Controller;

import com.microService.demo.dto.EstadoCuentaDTO;
import com.microService.demo.services.IMovimientoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private IMovimientoServices movimientoService;

    @GetMapping
    public ResponseEntity<EstadoCuentaDTO> generarReporteEstadoCuenta(
            @RequestParam("clienteId") String clienteId,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        try {
            EstadoCuentaDTO reporte = movimientoService.generarReporteEstadoCuenta(clienteId, fechaInicio, fechaFin);
            return new ResponseEntity<>(reporte, HttpStatus.OK);
        } catch (Exception e) {
            // Como solo implementamos hasta F3, esta funcionalidad puede estar incompleta
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
