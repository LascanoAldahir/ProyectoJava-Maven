//ClienteDTO
package com.microService.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$",
            message = "El nombre solo puede contener letras y espacios (2-50 caracteres)")
    private String nombre;

    @NotBlank(message = "El género es obligatorio")
    @Pattern(regexp = "^(?i)(masculino|femenino)$",
            message = "El género debe ser: Masculino o Femenino")
    private String genero;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 120, message = "La edad máxima es 120 años")
    private Integer edad;  // Mantener como Integer, NO String

    @NotNull(message = "La identificación es obligatoria")
    @Min(value = 10000000, message = "La identificación debe tener al menos 8 dígitos")
    @Max(value = 999999999999999L, message = "La identificación no puede exceder 15 dígitos")
    private Long identificacion;  // Cambiar a Long para validar rangos

    @NotBlank(message = "La dirección es obligatoria")
    @Size(min = 5, max = 150, message = "La dirección debe tener entre 5 y 150 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\.,#-]{5,150}$",
            message = "La dirección contiene caracteres no válidos")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\d{10}$",
            message = "El teléfono debe contener exactamente 10 dígitos numéricos")
    private String telefono;

    @NotBlank(message = "El ID del cliente es obligatorio")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$",
            message = "El ID del cliente solo puede contener letras y números (3-20 caracteres, sin espacios)")
    private String clienteId;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 50, message = "La contraseña debe tener entre 4 y 50 caracteres")
    private String contrasena;

    private boolean estado = true;

    // Método para normalizar género
    public void setGenero(String genero) {
        if (genero != null) {
            this.genero = genero.toLowerCase().trim();
            if (this.genero.equals("masculino") || this.genero.equals("femenino")) {
                this.genero = this.genero.substring(0, 1).toUpperCase() + this.genero.substring(1);
            }
        }
    }
}