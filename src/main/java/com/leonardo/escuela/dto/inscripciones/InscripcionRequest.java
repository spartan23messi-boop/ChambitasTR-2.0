package com.leonardo.escuela.dto.inscripciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos para registrar o actualizar una inscripción")
public record InscripcionRequest(
        @Schema(description = "ID de un alumno existente", example = "1")
        @NotNull(message = "El alumno es requerido")
        @Positive(message = "El ID del alumno debe ser positivo")
        Long idAlumno,

        @Schema(description = "ID de un grupo existente", example = "1")
        @NotNull(message = "El grupo es requerido")
        @Positive(message = "El ID del grupo debe ser positivo")
        Long idGrupo
) {}
