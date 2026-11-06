package com.leonardo.escuela.utils;

import com.leonardo.escuela.exceptions.RecursoNoEncontradoException;
import com.leonardo.escuela.entities.Alumno;
import com.leonardo.escuela.repositories.AlumnoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

@Slf4j
public class ServiceUtils {

    public static Alumno obtenerAlumno(AlumnoRepository alumnoRepository, Long idAlumno) {
        return obtenerEntidadOException(alumnoRepository, idAlumno, Alumno.class);
    }

    public static <E, ID> E obtenerEntidadOException(
            JpaRepository<E, ID> repository,
            ID id,
            Class<E> clase) {

        log.info("Buscando {} con el id: {}",
                clase.getSimpleName(), id);

        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        clase.getSimpleName() +
                                " no encontrado con id: " + id
                ));
    }

}
