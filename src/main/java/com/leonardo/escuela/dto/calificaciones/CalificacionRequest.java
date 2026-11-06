package com.leonardo.escuela.dto.calificaciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Datos para registrar o actualizar una calificación")
public record CalificacionRequest(
        @Schema(description = "ID de una inscripción existente", example = "1")
        @NotNull(message = "La inscripción es requerida")
        @Positive(message = "El ID de la inscripción debe ser positivo")
        Long idInscripcion,

        @Schema(description = "Calificación numérica", example = "9.5")
        @NotNull(message = "La calificación es requerida")
        @DecimalMin(value = "0.0", message = "La calificación no puede ser negativa")
        @DecimalMax(value = "99.9", message = "La calificación no puede superar 99.9")
        @Digits(integer = 2, fraction = 1, message = "La calificación admite hasta 2 enteros y 1 decimal")
        BigDecimal calificacion
) {}
