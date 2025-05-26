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
        // Obtener la cuenta asociada al movimiento
        Cuenta cuenta = cuentaRepository.findById(movimientoDTO.getNumeroCuenta())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        // Verificar si hay saldo disponible para retiros
        double saldoActual = cuenta.getSaldoInicial();
        double valor = movimientoDTO.getValor();

        // Si es un retiro (valor negativo) y no hay saldo suficiente
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
            // Obtener el movimiento original para comparar
            Movimiento originalMovimiento = movimientoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

            // Obtener la cuenta asociada al movimiento
            Cuenta cuenta = cuentaRepository.findById(movimientoDTO.getNumeroCuenta())
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

            // Calcular la diferencia entre el valor original y el nuevo
            double diferencia = movimientoDTO.getValor() - originalMovimiento.getValor();

            // Verificar si hay saldo disponible para la diferencia (si es negativa)
            double saldoActual = cuenta.getSaldoInicial();
            if (diferencia < 0 && (saldoActual + diferencia) < 0) {
                throw new RuntimeException("Saldo no disponible");
            }

            // Actualizar el movimiento
            Movimiento movimiento = new Movimiento();
            movimiento.setId(id);
            movimiento.setFecha(movimientoDTO.getFecha());
            movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
            movimiento.setValor(movimientoDTO.getValor());
            movimiento.setCuenta(cuenta);

            // Calcular nuevo saldo
            double nuevoSaldo = saldoActual + diferencia;
            movimiento.setSaldo(nuevoSaldo);

            // Actualizar saldo de la cuenta
            cuenta.setSaldoInicial(nuevoSaldo);
            cuentaRepository.save(cuenta);

            // Guardar cambios
            Movimiento updatedMovimiento = movimientoRepository.save(movimiento);
            return convertToDTO(updatedMovimiento);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        // Verificar que el movimiento existe
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // Actualizar el saldo de la cuenta
        Cuenta cuenta = movimiento.getCuenta();
        if (cuenta != null) {
            cuenta.setSaldoInicial(cuenta.getSaldoInicial() - movimiento.getValor());
            cuentaRepository.save(cuenta);
        }

        // Eliminar el movimiento
        movimientoRepository.deleteById(id);
    }

    @Override
    public EstadoCuentaDTO generarReporteEstadoCuenta(String clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        // Implementación básica por ahora ya que solo estamos hasta F3
        return new EstadoCuentaDTO();
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