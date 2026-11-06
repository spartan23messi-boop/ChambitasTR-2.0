package com.leonardo.escuela.dto.inscripciones;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos para registrar o actualizar una inscripción")
public record InscripcionResponse(
        Long idInscripcion, Long idAlumno,
        Long idGrupo,LocalDate fechaInscripcion,
        BigDecimal calificacion) {}
