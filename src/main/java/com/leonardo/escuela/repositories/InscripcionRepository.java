package com.leonardo.escuela.repositories;

import com.leonardo.escuela.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    boolean existsByAlumnoIdAlumno(Long idAlumno);

    boolean existsByGrupoIdGrupo(Long idGrupo);
    boolean existsByAlumnoIdAlumnoAndGrupoIdGrupo(Long alumno, Long grupo);
    boolean existsByAlumnoIdAlumnoAndGrupoIdGrupoAndIdInscripcionNot(Long alumno, Long grupo, Long id);
}
