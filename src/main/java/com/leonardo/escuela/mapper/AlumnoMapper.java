
package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.alumnos.AlumnoRequest;
import com.leonardo.escuela.dto.alumnos.AlumnoResponse;
import com.leonardo.escuela.dto.datos.DatosCalificacion;
import com.leonardo.escuela.entities.Alumno;
import com.leonardo.escuela.utils.StringCustomUtils;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AlumnoMapper
        implements CommonMapper<AlumnoRequest, AlumnoResponse, Alumno> {

    @Override
    public Alumno requestAEntidad(AlumnoRequest request) {
        return request == null
                ? null
                : Alumno.crear(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno()
        );
    }

    private List<DatosCalificacion> entidadADatosCalificacion(Alumno entidad){

        if (entidad == null || entidad.getInscripciones() == null || entidad.getInscripciones().isEmpty())
            return List.of();

        return entidad.getInscripciones().stream().map(inscripcion -> new DatosCalificacion(
                inscripcion.getGrupo().getCurso().getNombre(),
                inscripcion.getGrupo().getPeriodo(),
                inscripcion.getCalificacion() != null
                        ? inscripcion.getCalificacion().getCalificacion() : null
        )).toList();
    }

    @Override
    public AlumnoResponse entidadAResponse(Alumno entidad) {
        return entidad == null ? null : new AlumnoResponse(
                entidad.getIdAlumno(),
                entidad.obtenerNombreCompleto(),
                entidad.getEmail(),
                entidad.getMatricula(),
                StringCustomUtils.localDateAString(entidad.getFechaIngreso()),
                entidadADatosCalificacion(entidad),
                entidad.calcularPromedio()
        );
    }
}
