package com.microService.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;
    private String nombre;
    private String genero;
    private int edad;
    private int identificacion;
    private String direccion;
    private String telefono;
    private String clienteId;
    private String contrasena;
    private boolean estado;

    // Este DTO incluye todos los campos necesarios para transferir 
    // datos de Cliente entre diferentes capas de la aplicación
}