package com.leonardo.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

public record DatosCurso(

        @Schema(description = "Nombre del curso", example = "Matematicas 1")
        String nombre,

        @Schema(description = "Descripcion del curso",example = "Curso de calculo integral")
        String descripcion,

        @Schema(description = "Creditos del curso", example = "5")
        Integer creditos
) {}
