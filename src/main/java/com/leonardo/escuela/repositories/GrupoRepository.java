package com.leonardo.escuela.repositories;

import com.leonardo.escuela.entities.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    boolean existsByMaestroIdMaestro(Long idMaestro);

    boolean existsByAulaIdAula(Long idAula);

    boolean existsByCursoIdCurso(Long idCurso);

    boolean existsByCursoIdCursoAndMaestroIdMaestroAndAulaIdAulaAndPeriodo(
            Long idCurso, Long idMaestro, Long idAula, String periodo
    );

    boolean existsByCursoIdCursoAndMaestroIdMaestroAndAulaIdAulaAndPeriodoAndIdGrupoNot(
            Long idCurso, Long idMaestro, Long idAula,
            String periodo, Long idGrupo
    );

    }


