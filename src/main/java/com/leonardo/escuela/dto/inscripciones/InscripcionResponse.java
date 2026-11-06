package com.leonardo.escuela.dto.inscripciones;
import java.math.BigDecimal;
import java.time.LocalDate;
public record InscripcionResponse(Long idInscripcion, Long idAlumno, Long idGrupo,
                                  LocalDate fechaInscripcion, BigDecimal calificacion) {}
