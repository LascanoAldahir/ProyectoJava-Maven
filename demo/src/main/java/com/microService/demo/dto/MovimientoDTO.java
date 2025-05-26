//MovimientoDTO

package com.microService.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDTO {

    private Long id;
    private LocalDate fecha;
    private String tipoMovimiento;
    private double valor;
    private double saldo;
    private Long numeroCuenta; // Solo mantenemos el número de cuenta, no el objeto completo

    // Este DTO simplifica la transferencia de datos de movimientos
    // evitando referencias circulares con cuentas
}