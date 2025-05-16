package com.microService.demo.Controller;

import com.microService.demo.dto.CuentaDTO;
import com.microService.demo.services.ICuentaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {
    @Autowired
    private ICuentaServices cuentaService;

    @GetMapping
    public ResponseEntity<List<CuentaDTO>> getAllCuentas() {
        List<CuentaDTO> cuentas = cuentaService.findAll();
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaDTO> getCuentaByNumeroCuenta(@PathVariable Long numeroCuenta) {
        CuentaDTO cuenta = cuentaService.findByNumeroCuenta(numeroCuenta);
        if (cuenta != null) {
            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaDTO>> getCuentasByClienteId(@PathVariable Long clienteId) {
        List<CuentaDTO> cuentas = cuentaService.findByClienteId(clienteId);
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CuentaDTO> createCuenta(@RequestBody CuentaDTO cuentaDTO) {
        CuentaDTO newCuenta = cuentaService.save(cuentaDTO);
        return new ResponseEntity<>(newCuenta, HttpStatus.CREATED);
    }

    @PutMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaDTO> updateCuenta(@PathVariable Long numeroCuenta, @RequestBody CuentaDTO cuentaDTO) {
        CuentaDTO updatedCuenta = cuentaService.update(numeroCuenta, cuentaDTO);
        if (updatedCuenta != null) {
            return new ResponseEntity<>(updatedCuenta, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long numeroCuenta) {
        cuentaService.delete(numeroCuenta);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
