
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

