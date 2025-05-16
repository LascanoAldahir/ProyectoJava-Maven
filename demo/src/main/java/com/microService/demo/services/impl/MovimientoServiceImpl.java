package com.microService.demo.services.impl;

import com.microService.demo.dto.MovimientoDTO;
import com.microService.demo.model.Cuenta;
import com.microService.demo.model.Movimiento;
import com.microService.demo.repository.ICuentaRepository;
import com.microService.demo.repository.IMovimientoRepository;
import com.microService.demo.services.IMovimientoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoServiceImpl implements IMovimientoServices {
    @Autowired
    private IMovimientoRepository movimientoRepository;

    @Autowired
    private ICuentaRepository cuentaRepository;

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
        Movimiento movimiento = convertToEntity(movimientoDTO);

        // Establecemos la fecha actual si no se proporciona
        if (movimiento.getFecha() == null) {
            movimiento.setFecha(LocalDate.now());
        }

        // Obtener la cuenta y actualizar su saldo
        if (movimiento.getCuenta() != null) {
            Cuenta cuenta = cuentaRepository.findById(movimiento.getCuenta().getNumeroCuenta())
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

            // Calculamos el nuevo saldo
            double nuevoSaldo = cuenta.getSaldoInicial() + movimiento.getValor();
            movimiento.setSaldo(nuevoSaldo);

            // Actualizamos el saldo de la cuenta
            cuenta.setSaldoInicial(nuevoSaldo);
            cuentaRepository.save(cuenta);
        }

        // Guardamos el movimiento
        Movimiento savedMovimiento = movimientoRepository.save(movimiento);
        return convertToDTO(savedMovimiento);
    }

    @Override
    public MovimientoDTO update(Long id, MovimientoDTO movimientoDTO) {
        if (movimientoRepository.existsById(id)) {
            Movimiento movimiento = convertToEntity(movimientoDTO);
            movimiento.setId(id);
            Movimiento updatedMovimiento = movimientoRepository.save(movimiento);
            return convertToDTO(updatedMovimiento);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        movimientoRepository.deleteById(id);
    }

    private MovimientoDTO convertToDTO(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(movimiento.getId());
        dto.setFecha(movimiento.getFecha());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setValor(movimiento.getValor());
        dto.setSaldo(movimiento.getSaldo());

        if (movimiento.getCuenta() != null) {
            dto.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
        }
        return dto;
    }

    private Movimiento convertToEntity(MovimientoDTO dto) {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(dto.getId());
        movimiento.setFecha(dto.getFecha());
        movimiento.setTipoMovimiento(dto.getTipoMovimiento());
        movimiento.setValor(dto.getValor());
        movimiento.setSaldo(dto.getSaldo());

        if (dto.getNumeroCuenta() != null) {
            cuentaRepository.findById(dto.getNumeroCuenta())
                    .ifPresent(movimiento::setCuenta);
        }

        return movimiento;
    }
}
