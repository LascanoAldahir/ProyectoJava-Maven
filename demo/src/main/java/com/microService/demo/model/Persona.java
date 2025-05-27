package com.microService.demo.model;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor

public abstract class Persona {
    private String nombre;
    private String genero;
    private int edad;
    private long identificacion;
    private String direccion;
    private String telefono;
}
