package com.microService.demo.services;

import com.microService.demo.dto.EstadoCuentaDTO;
import com.microService.demo.dto.MovimientoDTO;
import java.time.LocalDate;
import java.util.List;

public interface IMovimientoServices {

    // Obtiene todos los movimientos
    List<MovimientoDTO> findAll();

    // Busca un movimiento por su ID
    MovimientoDTO findById(Long id);

    // Busca todos los movimientos de una cuenta específica
    List<MovimientoDTO> findByNumeroCuenta(Long numeroCuenta);

    // Busca movimientos de una cuenta en un rango de fechas
    List<MovimientoDTO> findByNumeroCuentaAndFechaBetween(
            Long numeroCuenta, LocalDate fechaInicio, LocalDate fechaFin);

    // Guarda un nuevo movimiento
    MovimientoDTO save(MovimientoDTO movimientoDTO);

    // Actualiza un movimiento existente
    MovimientoDTO update(Long id, MovimientoDTO movimientoDTO);

    // Elimina un movimiento
    void delete(Long id);

    // Nuevo metodo para generar reporte de estado de cuenta
    EstadoCuentaDTO generarReporteEstadoCuenta(String clienteId, LocalDate fechaInicio, LocalDate fechaFin);
}