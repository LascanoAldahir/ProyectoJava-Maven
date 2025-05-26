//Cuenta.java

package com.microService.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {

    @Id
    @Column(unique = true)
    private Long numeroCuenta;

    private String tipoCuenta;
    private double saldoInicial;
    private boolean estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // En la implementación actual, la cuenta tiene una relación directa con Cliente
    // Esto deberá cambiar cuando separemos en microservicios
}