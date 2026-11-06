package com.leonardo.escuela.mapper;
import com.leonardo.escuela.dto.calificaciones.*;
import com.leonardo.escuela.entities.*;
import org.springframework.stereotype.Component;
@Component
public class CalificacionMapper {
    public Calificacion requestAEntidad(CalificacionRequest request, Inscripcion inscripcion) {
        return request == null ? null : Calificacion.crear(inscripcion, request.calificacion());
    }
    public CalificacionResponse entidadAResponse(Calificacion entidad) {
        return entidad == null ? null : new CalificacionResponse(entidad.getIdCalificacion(), entidad.getInscripcion().getIdInscripcion(), entidad.getCalificacion(), entidad.getFechaRegistro());
    }
}
