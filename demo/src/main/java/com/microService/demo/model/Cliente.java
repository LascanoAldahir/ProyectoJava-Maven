//Cliente.java

package com.microService.demo.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends Persona{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String clienteId;

    private String contrasena;
    private boolean estado;

    // Constructor que incluye campos de Persona
    public Cliente(String nombre, String genero, int edad, long identificacion,
                   String direccion, String telefono, String clienteId,
                   String contrasena, boolean estado) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.clienteId = clienteId;
        this.contrasena = contrasena;
        this.estado = estado;
    }
    // Si no estás usando herencia de Persona, todos los campos van aquí
    // Si estás usando herencia, necesitaríamos la clase Persona y heredar de ella
}