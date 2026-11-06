package com.leonardo.escuela.dto.horarios;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos para registrar o actualizar un horario")
public record HorarioRequest(
        @Schema(description = "ID de un grupo existente", example = "1")
        @NotNull(message = "El grupo es requerido")
        @Positive(message = "El ID del grupo debe ser positivo")
        Long idGrupo,

        @Schema(description = "Día de la semana", example = "LUNES")
        @NotBlank(message = "El día es requerido")
        String dia,

        @Schema(description = "Hora de inicio", example = "08:00")
        @NotBlank(message = "La hora de inicio es requerida")
        @Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message = "La hora debe tener formato HH:mm")
        String horaInicio,

        @Schema(description = "Hora de fin", example = "10:00")
        @NotBlank(message = "La hora de fin es requerida")
        @Pattern(regexp = "([01][0-9]|2[0-3]):[0-5][0-9]", message = "La hora debe tener formato HH:mm")
        String horaFin
) {}
