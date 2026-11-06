package com.leonardo.escuela.dto.inscripciones;
import jakarta.validation.constraints.*;
public record InscripcionRequest(@NotNull @Positive Long idAlumno, @NotNull @Positive Long idGrupo) {}
