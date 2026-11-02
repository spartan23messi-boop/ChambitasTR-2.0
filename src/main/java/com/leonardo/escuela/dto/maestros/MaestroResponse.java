package com.leonardo.escuela.dto.maestros;

import com
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Informacion de un maestro")
public record MaestroResponse(

        @Schema(descripcion = "ID del maestro",example = "1")
        Long id;

        @Schema(descripcion = "Nombre completo del maestro",example = "Maximo Decimo Merido")
        String nombre;

        @Schema(descripcion = "Email del maestro",example)

) {}
