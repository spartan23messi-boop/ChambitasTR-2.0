package com.leonardo.escuela.enums;

import com.leonardo.escuela.exceptions.RecursoNoEncontradoException;
import com.leonardo.escuela.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DiaSemana {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miercoles"),
    JUEVES("Jueves"),
    VIERNES("VIERNES"),
    SABADO("Sabado");

    private final String descripcion;

    public static DiaSemana obternerDiaSemanaPorDescripcion(String descripcion){

        StringCustomUtils.validarNoVacio(descripcion, "La descripcion es requerida");

        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for(DiaSemana diaSemana : values()){
            if(StringCustomUtils.normalizarTexto(diaSemana.descripcion).equals(descripcionNormalizada))
                return diaSemana;
        }
        throw new RecursoNoEncontradoException("No existe un dia de la semana con la descripcion: "+descripcion);
    }
}
