package com.microService.demo.repository;

import com.microService.demo.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IMovimientoRepository extends JpaRepository<Movimiento, Long> {
    // Encuentra todos los movimientos de una cuenta específica
    List<Movimiento> findByCuentaNumeroCuenta(Long numeroCuenta);

    // Encuentra movimientos de una cuenta en un rango de fechas
    List<Movimiento> findByCuentaNumeroCuentaAndFechaBetween(
            Long numeroCuenta, LocalDate fechaInicio, LocalDate fechaFin);
}