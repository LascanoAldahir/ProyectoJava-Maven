package com.microService.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaSaldoDTO {
    private Long numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
    private double saldoFinal;
    private boolean estado;
}