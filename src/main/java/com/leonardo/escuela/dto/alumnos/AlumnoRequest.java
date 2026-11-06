package com.leonardo.escuela.dto.alumnos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para registrar o actualizar un alumno. Email y matrícula se generan en Oracle")
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
        String apellidoMaterno
) {}
