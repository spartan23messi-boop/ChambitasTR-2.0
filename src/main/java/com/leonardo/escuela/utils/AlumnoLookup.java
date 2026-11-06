package com.leonardo.escuela.utils;

import com.leonardo.escuela.entities.Alumno;
import com.leonardo.escuela.repositories.AlumnoRepository;

public final class AlumnoLookup {

    private AlumnoLookup() {
    }

    public static Alumno obtener(AlumnoRepository alumnoRepository, Long idAlumno) {
        return ServiceUtils.obtenerEntidadOException(alumnoRepository, idAlumno, Alumno.class);
    }
}
