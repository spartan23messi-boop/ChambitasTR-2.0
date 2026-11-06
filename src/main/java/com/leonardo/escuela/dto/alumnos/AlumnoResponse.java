package com.leonardo.escuela.dto.alumnos;

import com.leonardo.escuela.dto.datos.DatosCalificacion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Información del alumno")
public record AlumnoResponse(
        @Schema(description = "ID del alumno", example = "1") Long idAlumno,
        @Schema(description = "Nombre completo del alumno", example = "Máximo Décimo Meridio") String nombre,
        @Schema(description = "Email del alumno", example = "test@test.com") String email,
        @Schema(description = "Matrícula del alumno", example = "1234567890") String matricula,
        @Schema(description = "Fecha de ingreso del alumno", example = "2026-09-12") String fechaIngreso,
        @Schema(description = "Calificaciones del alumno") List<DatosCalificacion> calificaciones,
        @Schema(description = "Promedio del alumno", example = "9.9") BigDecimal promedio
) {}
