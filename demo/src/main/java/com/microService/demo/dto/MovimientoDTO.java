//MovimientoDTO

package com.microService.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDTO {

    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "^(?i)(deposito|depósito|retiro)$",
            message = "El tipo de movimiento debe ser: Deposito o Retiro")
    private String tipoMovimiento;

    @NotNull(message = "El valor es obligatorio")
    @Pattern(regexp = "^-?\\d+(\\.\\d{1,2})?$",
            message = "El valor debe ser un número válido (máximo 2 decimales)")
    private String valor;

    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$",
            message = "El saldo debe ser un número válido (máximo 2 decimales)")
    private String saldo;

    @NotNull(message = "El número de cuenta es obligatorio")
    @Pattern(regexp = "^\\d{6,12}$",
            message = "El número de cuenta debe contener entre 6 y 12 dígitos")
    private String numeroCuenta;

    // Métodos para normalizar y validar datos
    public void setTipoMovimiento(String tipoMovimiento) {
        if (tipoMovimiento != null) {
            String normalized = tipoMovimiento.toLowerCase()
                    .replaceAll("[áàâãä]", "a")
                    .replaceAll("[éèêë]", "e")
                    .replaceAll("[íìîï]", "i")
                    .replaceAll("[óòôõö]", "o")
                    .replaceAll("[úùûü]", "u")
                    .trim();

            if (normalized.equals("deposito") || normalized.equals("depósito")) {
                this.tipoMovimiento = "Deposito";
            } else if (normalized.equals("retiro")) {
                this.tipoMovimiento = "Retiro";
            }
        }
    }

    public void setValor(String valor) {
        if (valor != null) {
            // Para retiros, permitir números negativos
            this.valor = valor.replaceAll("[^0-9.-]", "");
        }
    }

    public void setSaldo(String saldo) {
        if (saldo != null) {
            this.saldo = saldo.replaceAll("[^0-9.]", "");
        }
    }

    // MÉTODO CORREGIDO: Acepta String y convierte a LocalDate
    public void setFechaFromString(String fechaStr) {
        if (fechaStr != null && !fechaStr.trim().isEmpty()) {
            try {
                // Validar formato YYYY-MM-DD
                if (fechaStr.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    this.fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
                } else {
                    throw new IllegalArgumentException("Formato de fecha inválido. Use: YYYY-MM-DD");
                }
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Fecha inválida. Use formato: YYYY-MM-DD (ej: 2024-05-19)");
            }
        }
    }

    // METODO ADICIONAL: Setter normal para LocalDate
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    // Métodos de conversión
    public double getValorAsDouble() {
        try {
            return Double.parseDouble(this.valor);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public double getSaldoAsDouble() {
        try {
            return this.saldo != null ? Double.parseDouble(this.saldo) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public Long getNumeroCuentaAsLong() {
        try {
            return Long.parseLong(this.numeroCuenta);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Validación personalizada para retiros
    public boolean isValidRetiro() {
        if ("Retiro".equals(this.tipoMovimiento)) {
            double valorNum = getValorAsDouble();
            return valorNum < 0; // Los retiros deben ser negativos
        }
        return true;
    }

    // Validación personalizada para depósitos
    public boolean isValidDeposito() {
        if ("Deposito".equals(this.tipoMovimiento)) {
            double valorNum = getValorAsDouble();
            return valorNum > 0; // Los depósitos deben ser positivos
        }
        return true;
    }
}