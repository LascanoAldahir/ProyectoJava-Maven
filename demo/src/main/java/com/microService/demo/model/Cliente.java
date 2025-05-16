package com.microService.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String genero;
    private int edad;
    private int identificacion;
    private String direccion;
    private String telefono;

    @Column(unique = true)
    private String clienteId;

    private String contrasena;
    private boolean estado;

    // Si no estás usando herencia de Persona, todos los campos van aquí
    // Si estás usando herencia, necesitaríamos la clase Persona y heredar de ella
}