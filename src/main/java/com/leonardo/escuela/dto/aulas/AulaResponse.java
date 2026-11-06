package com.leonardo.escuela.dto.aulas;

import io.swagger.v3.oas.annotations.media.Schema;

public record AulaResponse(

        @Schema(description = "ID del aula", example = "1")
        Long idAula,

        @Schema(description = "Nombre del aula", example = "Laboratorio 1")
        String nombre,

        @Schema(description = "Capacidad del aula", example = "35")
        Integer capacidad

) {}