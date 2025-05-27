//MovimientoServiceImpl
package com.microService.demo.services.impl;

import com.microService.demo.dto.CuentaSaldoDTO;
import com.microService.demo.dto.EstadoCuentaDTO;
import com.microService.demo.dto.MovimientoDTO;
import com.microService.demo.model.Cuenta;
import com.microService.demo.model.Cliente;
import com.microService.demo.model.Movimiento;
import com.microService.demo.repository.IClienteRepository;
import com.microService.demo.repository.ICuentaRepository;
import com.microService.demo.repository.IMovimientoRepository;
import com.microService.demo.services.IMovimientoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoServiceImpl implements IMovimientoServices {

    @Autowired
    private IMovimientoRepository movimientoRepository;

    @Autowired
    private ICuentaRepository cuentaRepository;

    @Autowired
    private IClienteRepository clienteRepository;

    @Override
    public List<MovimientoDTO> findAll() {
        return movimientoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MovimientoDTO findById(Long id) {
        return movimientoRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<MovimientoDTO> findByNumeroCuenta(Long numeroCuenta) {
        return movimientoRepository.findByCuentaNumeroCuenta(numeroCuenta).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoDTO> findByNumeroCuentaAndFechaBetween(Long numeroCuenta, LocalDate fechaInicio, LocalDate fechaFin) {
        return movimientoRepository.findByCuentaNumeroCuentaAndFechaBetween(numeroCuenta, fechaInicio, fechaFin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MovimientoDTO save(MovimientoDTO movimientoDTO) {
        // Convertir String a Long para buscar la cuenta
        Long numeroCuenta = Long.parseLong(movimientoDTO.getNumeroCuenta());
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        // Convertir String a Double para el valor
        double valor = Double.parseDouble(movimientoDTO.getValor());
        double saldoActual = cuenta.getSaldoInicial();

        // Verificar saldo para retiros
        if (valor < 0 && (saldoActual + valor) < 0) {
            throw new RuntimeException("Saldo no disponible");
        }

        // Crear entidad de movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(movimientoDTO.getFecha() != null ? movimientoDTO.getFecha() : LocalDate.now());
        movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimiento.setValor(valor);
        movimiento.setCuenta(cuenta);

        // Calcular nuevo saldo
        double nuevoSaldo = saldoActual + valor;
        movimiento.setSaldo(nuevoSaldo);

        // Actualizar saldo de la cuenta
        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        // Guardar movimiento
        Movimiento savedMovimiento = movimientoRepository.save(movimiento);
        return convertToDTO(savedMovimiento);
    }

    @Override
    public MovimientoDTO update(Long id, MovimientoDTO movimientoDTO) {
        if (movimientoRepository.existsById(id)) {
            Movimiento originalMovimiento = movimientoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

            Long numeroCuenta = Long.parseLong(movimientoDTO.getNumeroCuenta());
            Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

            double nuevoValor = Double.parseDouble(movimientoDTO.getValor());
            double diferencia = nuevoValor - originalMovimiento.getValor();

            double saldoActual = cuenta.getSaldoInicial();
            if (diferencia < 0 && (saldoActual + diferencia) < 0) {
                throw new RuntimeException("Saldo no disponible");
            }

            Movimiento movimiento = new Movimiento();
            movimiento.setId(id);
            movimiento.setFecha(movimientoDTO.getFecha());
            movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
            movimiento.setValor(nuevoValor);
            movimiento.setCuenta(cuenta);

            double nuevoSaldo = saldoActual + diferencia;
            movimiento.setSaldo(nuevoSaldo);

            cuenta.setSaldoInicial(nuevoSaldo);
            cuentaRepository.save(cuenta);

            Movimiento updatedMovimiento = movimientoRepository.save(movimiento);
            return convertToDTO(updatedMovimiento);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        Cuenta cuenta = movimiento.getCuenta();
        if (cuenta != null) {
            cuenta.setSaldoInicial(cuenta.getSaldoInicial() - movimiento.getValor());
            cuentaRepository.save(cuenta);
        }

        movimientoRepository.deleteById(id);
    }

    @Override
    public EstadoCuentaDTO generarReporteEstadoCuenta(String clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        return new EstadoCuentaDTO();
    }

    // MÉTODO CORREGIDO: Convierte Double/Long a String
    private MovimientoDTO convertToDTO(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(movimiento.getId());
        dto.setFecha(movimiento.getFecha());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setValor(String.valueOf(movimiento.getValor())); // Double -> String
        dto.setSaldo(String.valueOf(movimiento.getSaldo())); // Double -> String

        if (movimiento.getCuenta() != null) {
            dto.setNumeroCuenta(String.valueOf(movimiento.getCuenta().getNumeroCuenta())); // Long -> String
        }
        return dto;
    }

    // MÉTODO CORREGIDO: Convierte String a Double/Long
    private Movimiento convertToEntity(MovimientoDTO dto) {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(dto.getId());
        movimiento.setFecha(dto.getFecha());
        movimiento.setTipoMovimiento(dto.getTipoMovimiento());

        // Convertir String a Double
        try {
            movimiento.setValor(Double.parseDouble(dto.getValor()));
            if (dto.getSaldo() != null) {
                movimiento.setSaldo(Double.parseDouble(dto.getSaldo()));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Valor numérico inválido");
        }

        // Convertir String a Long para buscar cuenta
        if (dto.getNumeroCuenta() != null) {
            try {
                Long numeroCuenta = Long.parseLong(dto.getNumeroCuenta());
                cuentaRepository.findById(numeroCuenta)
                        .ifPresent(movimiento::setCuenta);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Número de cuenta inválido");
            }
        }

        return movimiento;
    }
}