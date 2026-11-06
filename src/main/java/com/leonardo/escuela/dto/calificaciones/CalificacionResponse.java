package com.leonardo.escuela.dto.calificaciones;
import java.math.BigDecimal;
import java.time.LocalDate;
public record CalificacionResponse(Long idCalificacion, Long idInscripcion,
                                   BigDecimal calificacion, LocalDate fechaRegistro) {}
