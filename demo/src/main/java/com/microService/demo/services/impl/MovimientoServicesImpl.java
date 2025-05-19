package com.microService.demo.services.impl;

import com.microService.demo.dto.MovimientoDTO;
import com.microService.demo.model.Cuenta;
import com.microService.demo.model.Movimiento;
import com.microService.demo.repository.ICuentaRepository;
import com.microService.demo.repository.IMovimientoRepository;
import com.microService.demo.services.IMovimientoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoServicesImpl implements IMovimientoServices {

    // Inyectamos el repositorio de movimientos para operaciones CRUD
    @Autowired
    private IMovimientoRepository movimientoRepository;

    // Inyectamos el repositorio de cuentas para acceder a las cuentas asociadas
    @Autowired
    private ICuentaRepository cuentaRepository;

    @Override
    public List<MovimientoDTO> findAll() {
        // Obtenemos todos los movimientos y los convertimos a DTOs
        return movimientoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MovimientoDTO findById(Long id) {
        // Buscamos un movimiento por su ID
        return movimientoRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<MovimientoDTO> findByNumeroCuenta(Long numeroCuenta) {
        // Buscamos todos los movimientos de una cuenta específica
        return movimientoRepository.findByCuentaNumeroCuenta(numeroCuenta).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoDTO> findByNumeroCuentaAndFechaBetween(Long numeroCuenta, LocalDate fechaInicio, LocalDate fechaFin) {
        // Buscamos movimientos de una cuenta en un rango de fechas específico
        return movimientoRepository.findByCuentaNumeroCuentaAndFechaBetween(numeroCuenta, fechaInicio, fechaFin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Añadimos @Transactional para garantizar que todas las operaciones de BD se completen o ninguna
    @Override
    @Transactional
    public MovimientoDTO save(MovimientoDTO movimientoDTO) {
        // Convertimos el DTO a entidad para trabajar con ella
        Movimiento movimiento = convertToEntity(movimientoDTO);

        // Establecemos la fecha actual si no se proporciona
        if (movimiento.getFecha() == null) {
            movimiento.setFecha(LocalDate.now());
        }

        // Validamos que el tipo de movimiento sea coherente con el valor
        validarTipoMovimiento(movimiento);

        // Obtenemos la cuenta asociada o lanzamos una excepción si no existe
        if (movimiento.getCuenta() != null) {
            Cuenta cuenta = cuentaRepository.findById(movimiento.getCuenta().getNumeroCuenta())
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

            // F3: Verificamos si hay saldo suficiente para retiros
            if (movimiento.getTipoMovimiento().equalsIgnoreCase("Retiro") &&
                    Math.abs(movimiento.getValor()) > cuenta.getSaldoInicial()) {
                // Si no hay saldo suficiente, lanzamos una excepción con el mensaje específico
                throw new RuntimeException("Saldo no disponible");
            }

            // F2: Actualización del saldo disponible según el tipo de movimiento
            // Si es depósito (valor positivo), se suma al saldo
            // Si es retiro (valor negativo), se resta del saldo
            double nuevoSaldo = cuenta.getSaldoInicial() + movimiento.getValor();

            // Asignamos el nuevo saldo al movimiento para tener un registro del saldo resultante
            movimiento.setSaldo(nuevoSaldo);

            // Actualizamos el saldo de la cuenta en la base de datos
            cuenta.setSaldoInicial(nuevoSaldo);
            cuentaRepository.save(cuenta);

            // También implementamos F3 aquí: Alerta cuando no hay saldo disponible
            // Este código se complementará más adelante
        }

        // Guardamos el movimiento en la base de datos
        Movimiento savedMovimiento = movimientoRepository.save(movimiento);

        // Convertimos la entidad guardada de vuelta a DTO y la devolvemos
        return convertToDTO(savedMovimiento);
    }

    /**
     * Valida que el tipo de movimiento (Depósito/Retiro) coincida con el valor (positivo/negativo)
     * Esta es la parte principal de la implementación de F2
     */
    private void validarTipoMovimiento(Movimiento movimiento) {
        // Si el tipo es "Depósito", el valor debe ser positivo
        if ("Depósito".equalsIgnoreCase(movimiento.getTipoMovimiento()) && movimiento.getValor() <= 0) {
            throw new RuntimeException("Un depósito debe tener un valor positivo");
        }

        // Si el tipo es "Retiro", el valor debe ser negativo
        if ("Retiro".equalsIgnoreCase(movimiento.getTipoMovimiento()) && movimiento.getValor() >= 0) {
            // Convertimos automáticamente el valor a negativo para asegurar consistencia
            movimiento.setValor(-Math.abs(movimiento.getValor()));
        }
    }

    @Override
    @Transactional
    public MovimientoDTO update(Long id, MovimientoDTO movimientoDTO) {
        // Verificamos que el movimiento exista
        if (movimientoRepository.existsById(id)) {
            // Para actualizar, primero obtenemos el movimiento original
            Movimiento movimientoOriginal = movimientoRepository.findById(id).orElseThrow();

            // Obtenemos el valor original para calcular el ajuste
            double valorOriginal = movimientoOriginal.getValor();

            // Convertimos el DTO con los nuevos datos a entidad
            Movimiento movimiento = convertToEntity(movimientoDTO);
            movimiento.setId(id);

            // Validamos el tipo de movimiento actualizado
            validarTipoMovimiento(movimiento);

            // Si cambia el valor, debemos ajustar el saldo de la cuenta
            if (valorOriginal != movimiento.getValor() && movimiento.getCuenta() != null) {
                Cuenta cuenta = cuentaRepository.findById(movimiento.getCuenta().getNumeroCuenta())
                        .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
                // Verificamos si hay saldo suficiente para retiros (F3)
                if (movimiento.getTipoMovimiento().equalsIgnoreCase("Retiro") &&
                        (cuenta.getSaldoInicial() - valorOriginal) < Math.abs(movimiento.getValor())) {
                    throw new RuntimeException("Saldo no disponible");
                }

                // Eliminamos el efecto del valor anterior y aplicamos el nuevo
                double ajuste = movimiento.getValor() - valorOriginal;
                double nuevoSaldo = cuenta.getSaldoInicial() + ajuste;

                // Actualizamos los saldos
                movimiento.setSaldo(nuevoSaldo);
                cuenta.setSaldoInicial(nuevoSaldo);
                cuentaRepository.save(cuenta);
            }

            // Guardamos el movimiento actualizado
            Movimiento updatedMovimiento = movimientoRepository.save(movimiento);
            return convertToDTO(updatedMovimiento);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Antes de eliminar, podríamos revertir el efecto en el saldo de la cuenta
        // Esto es opcional, depende de los requisitos del negocio
        Movimiento movimiento = movimientoRepository.findById(id).orElse(null);
        if (movimiento != null && movimiento.getCuenta() != null) {
            Cuenta cuenta = cuentaRepository.findById(movimiento.getCuenta().getNumeroCuenta()).orElse(null);
            if (cuenta != null) {
                // Revertimos el efecto del movimiento en el saldo
                double nuevoSaldo = cuenta.getSaldoInicial() - movimiento.getValor();
                cuenta.setSaldoInicial(nuevoSaldo);
                cuentaRepository.save(cuenta);
            }
        }

        // Eliminamos el movimiento
        movimientoRepository.deleteById(id);
    }

    // Metodo auxiliar para convertir una entidad Movimiento a MovimientoDTO
    private MovimientoDTO convertToDTO(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(movimiento.getId());
        dto.setFecha(movimiento.getFecha());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setValor(movimiento.getValor());
        dto.setSaldo(movimiento.getSaldo());

        // Si el movimiento tiene una cuenta asociada, incluimos su número
        if (movimiento.getCuenta() != null) {
            dto.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
        }
        return dto;
    }

    // Método auxiliar para convertir un MovimientoDTO a entidad Movimiento
    private Movimiento convertToEntity(MovimientoDTO dto) {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(dto.getId());
        movimiento.setFecha(dto.getFecha());
        movimiento.setTipoMovimiento(dto.getTipoMovimiento());
        movimiento.setValor(dto.getValor());
        movimiento.setSaldo(dto.getSaldo());

        // Si el DTO tiene un número de cuenta, buscamos la cuenta correspondiente
        if (dto.getNumeroCuenta() != null) {
            cuentaRepository.findById(dto.getNumeroCuenta())
                    .ifPresent(movimiento::setCuenta);
        }

        return movimiento;
    }
}