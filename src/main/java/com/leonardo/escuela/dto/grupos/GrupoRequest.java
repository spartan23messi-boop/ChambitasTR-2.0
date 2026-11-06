package com.leonardo.escuela.dto.grupos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Datos para registrar o actualizar un grupo")
public record GrupoRequest(

        @Schema(description = "ID del curso", example = "2")
        @NotNull(message = "El ID del curso es requerido")
        @Positive(message = "El ID del curso debe ser positivo")
        Long idCurso,

        @Schema(description = "ID del maestro", example = "1")
        @NotNull(message = "El ID del maestro es requerido")
        @Positive(message = "El ID del maestro debe ser positivo")
        Long idMaestro,

        @Schema(description = "ID del aula", example = "3")
        @NotNull(message = "El ID del aula es requerido")
        @Positive(message = "El ID del aula debe ser positivo")
        Long idAula,

        @Schema(description = "Período escolar", example = "2026-02")
        @NotBlank(message = "El período es requerido")
        @Size(max = 20, message = "El período debe tener máximo 20 caracteres")
        @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "El período debe tener formato yyyy-MM")
        String periodo

) {}