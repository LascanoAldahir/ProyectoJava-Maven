//CuentaDTO

package com.microService.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CuentaDTO {
    private Long numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
    private boolean estado;
    private String clienteId; // Solo mantenemos el ID del cliente, no el objeto completo
    // Este DTO simplifica la transferencia de datos de cuentas
    // evitando referencias circulares con clientes
}