//CuentaDTO

package com.microService.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CuentaDTO {
    @NotNull(message = "El número de cuenta es obligatorio")
    @Pattern(regexp = "^\\d{6,12}$",
            message = "El número de cuenta debe contener entre 6 y 12 dígitos numéricos")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Pattern(regexp = "^(?i)(ahorro|corriente|ahorros)$",
            message = "El tipo de cuenta debe ser: Ahorro, Corriente o Ahorros")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo inicial no puede ser negativo")
    @DecimalMax(value = "9999999.99", message = "El saldo inicial no puede exceder $9,999,999.99")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$",
            message = "El saldo inicial debe ser un número válido (máximo 2 decimales)")
    private String saldoInicial;

    private boolean estado = true;

    @NotBlank(message = "El ID del cliente es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$",
            message = "El ID del cliente solo puede contener letras y números")
    private String clienteId;

    // Métodos para normalizar valores
    public void setTipoCuenta(String tipoCuenta) {
        if (tipoCuenta != null) {
            String normalized = tipoCuenta.toLowerCase().trim();
            if (normalized.equals("ahorro") || normalized.equals("ahorros")) {
                this.tipoCuenta = "Ahorro";
            } else if (normalized.equals("corriente")) {
                this.tipoCuenta = "Corriente";
            }
        }
    }

    public void setSaldoInicial(String saldoInicial) {
        if (saldoInicial != null) {
            // Limpiar caracteres especiales excepto punto decimal
            this.saldoInicial = saldoInicial.replaceAll("[^0-9.]", "");
        }
    }

    // Método para obtener saldo como double
    public double getSaldoInicialAsDouble() {
        try {
            return Double.parseDouble(this.saldoInicial);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Método para obtener número de cuenta como Long
    public Long getNumeroCuentaAsLong() {
        try {
            return Long.parseLong(this.numeroCuenta);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}