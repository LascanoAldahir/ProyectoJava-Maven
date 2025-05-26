//MovimientoReporteDTO

package com.microService.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

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