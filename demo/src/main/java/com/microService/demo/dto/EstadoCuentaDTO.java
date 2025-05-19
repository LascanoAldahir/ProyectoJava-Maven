package com.microService.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoCuentaDTO {
    //Datos del cliente
    private String cliente;
    private Long clienteId;

    //Fecha del reporte
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    //Lista de cuentas con sus saldos
    private List<CuentaSaldoDTO> cuentas;

    //Detalle de todos los movimientos en el periodo
    private List<MovimientoReporteDTO> movimientos;
}
// DTO para mostrar cuentas y saldos en el reporte
@Data
@NoArgsConstructor
@AllArgsConstructor
class CuentaSaldoDTO {
    private Long numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
    private double saldoFinal;
    private boolean estado;
}

// DTO para mostrar movimientos en el reporte
@Data
@NoArgsConstructor
@AllArgsConstructor
class MovimientoReporteDTO {
    private LocalDate fecha;
    private Long numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
    private String tipoMovimiento;
    private double valor;
    private double saldoDisponible;
    private boolean estado;
}