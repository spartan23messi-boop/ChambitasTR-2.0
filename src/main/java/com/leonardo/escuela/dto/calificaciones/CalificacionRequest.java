package com.leonardo.escuela.dto.calificaciones;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CalificacionRequest(@NotNull @Positive Long idInscripcion,
                                  @NotNull @DecimalMin("0.0") @DecimalMax("99.9") @Digits(integer=2,fraction=1) BigDecimal calificacion) {}
