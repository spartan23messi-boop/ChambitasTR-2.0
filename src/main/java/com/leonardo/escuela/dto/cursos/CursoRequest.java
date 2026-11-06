package com.leonardo.escuela.dto.cursos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para registrar o actualizar un curso")
public record CursoRequest(
        @Schema(description = "Nombre único del curso", example = "Matemáticas I")
        @NotBlank(message = "El nombre es requerido")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @Schema(description = "Descripción opcional", example = "Introducción al álgebra")
        @Size(max = 200, message = "La descripción no puede superar 200 caracteres")
        String descripcion,

        @Schema(description = "Créditos del curso", example = "5")
        @NotNull(message = "Los créditos son requeridos")
        @Min(value = 1, message = "Los créditos deben ser al menos 1")
        @Max(value = 99, message = "Los créditos no pueden superar 99")
        Integer creditos
) {}
