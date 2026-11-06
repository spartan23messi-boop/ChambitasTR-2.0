package com.leonardo.escuela.dto.horarios;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description="Datos para registrar o actualizar un horario")
public record HorarioRequest(
    @NotNull @Positive Long idGrupo,
    @NotBlank String dia,
    @NotBlank @Pattern(regexp="([01][0-9]|2[0-3]):[0-5][0-9]",message="La hora debe tener formato HH:mm") String horaInicio,
    @NotBlank @Pattern(regexp="([01][0-9]|2[0-3]):[0-5][0-9]",message="La hora debe tener formato HH:mm") String horaFin
) {}
