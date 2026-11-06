package com.leonardo.escuela.repositories;

import com.leonardo.escuela.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdCursoNot(
            String nombre, Long idCurso
    );
}