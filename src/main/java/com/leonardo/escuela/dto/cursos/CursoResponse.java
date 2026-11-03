package com.leonardo.escuela.dto.cursos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un curso")
public record CursoResponse(
        @Schema(description = "Identificador del curso", example = "1")
        Long id,
        @Schema(description = "Nombre del curso", example = "Matemáticas I")
        String nombre,
        @Schema(description = "Descripción del curso", example = "Introducción al álgebra")
        String descripcion,
        @Schema(description = "Créditos del curso", example = "5")
        Integer creditos
) {}
