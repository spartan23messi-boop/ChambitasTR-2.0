package com.leonardo.escuela.dto.maestros;

import com.leonardo.escuela.dto.datos.DatosCurso;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Datos del maestro y los cursos que imparte")
public record MaestroResponse(
        @Schema(description = "ID del maestro", example = "1") Long id,
        @Schema(description = "Nombre completo", example = "Máximo Décimo Meridio") String nombre,
        @Schema(description = "Email", example = "maximo@escuela.com") String email,
        @Schema(description = "Teléfono", example = "2221234567") String telefono,
        @Schema(description = "Cursos impartidos por el maestro") List<DatosCurso> cursos
) {}
