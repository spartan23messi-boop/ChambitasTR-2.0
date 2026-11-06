package com.leonardo.escuela.mapper;
import com.leonardo.escuela.dto.inscripciones.*;
import com.leonardo.escuela.entities.*;
import org.springframework.stereotype.Component;
@Component
public class InscripcionMapper {
    public Inscripcion requestAEntidad(InscripcionRequest request, Alumno alumno, Grupo grupo) {
        return request == null ? null : Inscripcion.crear(alumno, grupo);
    }
    public InscripcionResponse entidadAResponse(Inscripcion entidad) {
        return entidad == null ? null : new InscripcionResponse(entidad.getIdInscripcion(), entidad.getAlumno().getIdAlumno(), entidad.getGrupo().getIdGrupo(), entidad.getFechaInscripcion(), entidad.getCalificacion() == null ? null : entidad.getCalificacion().getCalificacion());
    }
}
