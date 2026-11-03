
package com.leonardo.escuela.dto.alumno;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para registrar o actualizar un alumno")
public record AlumnoRequest(

        @Schema(description = "Nombre del alumno", example = "Maximo")
        @NotBlank(message = "El nombre es requerido")
        @Size(max = 50, message = "El nombre debe tener máximo 50 caracteres")
        String nombre,

        @Schema(description = "Apellido paterno", example = "Decimo")
        @NotBlank(message = "El apellido paterno es requerido")
        @Size(max = 50, message = "El apellido paterno debe tener máximo 50 caracteres")
        String apellidoPaterno,

        @Schema(description = "Apellido materno", example = "Meridio")
        @NotBlank(message = "El apellido materno es requerido")
        @Size(max = 50, message = "El apellido materno debe tener máximo 50 caracteres")
        String apellidoMaterno,

        @Schema(description = "Correo del alumno", example = "maximo@escuela.com")
        @NotBlank(message = "El correo es requerido")
        @Email(message = "El correo no tiene un formato válido")
        @Size(max = 100, message = "El correo debe tener máximo 100 caracteres")
        String email,

        @Schema(description = "Matrícula del alumno", example = "A2026001")
        @NotBlank(message = "La matrícula es requerida")
        @Size(max = 10, message = "La matrícula debe tener máximo 10 caracteres")
        String matricula

) {}
