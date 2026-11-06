package com.leonardo.escuela.dto.aulas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para registrar o actualizar un aula")
public record AulaRequest(

        @Schema(description = "Nombre del aula", example = "Laboratorio 1")
        @NotBlank(message = "El nombre del aula es requerido")
        @Size(max = 30, message = "El nombre debe tener máximo 30 caracteres")
        String nombre,

        @Schema(description = "Capacidad del aula", example = "35")
        @NotNull(message = "La capacidad del aula es requerida")
        @Min(value = 1, message = "La capacidad mínima es 1")
        @Max(value = 9999, message = "La capacidad máxima es 9999")
        Integer capacidad

) {}