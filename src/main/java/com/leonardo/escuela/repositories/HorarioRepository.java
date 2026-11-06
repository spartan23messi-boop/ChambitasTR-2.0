package com.leonardo.escuela.repositories;

import com.leonardo.escuela.entities.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    boolean existsByGrupoIdGrupo(Long idGrupo);

    @Query("""
            SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END
            FROM Horario h
            WHERE h.grupo.idGrupo = :idGrupo
              AND h.dia = :dia
              AND h.horaInicio < :horaFin
              AND h.horaFin > :horaInicio
              AND (:idExcluido IS NULL OR h.idHorario <> :idExcluido)
            """)
    boolean existeTraslapePorGrupo(
            @Param("idGrupo") Long idGrupo,
            @Param("dia") String dia,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin,
            @Param("idExcluido") Long idExcluido
    );

    @Query("""
            SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END
            FROM Horario h
            WHERE h.grupo.aula.idAula = :idAula
              AND h.grupo.periodo = :periodo
              AND h.grupo.idGrupo <> :idGrupo
              AND h.dia = :dia
              AND h.horaInicio < :horaFin
              AND h.horaFin > :horaInicio
              AND (:idExcluido IS NULL OR h.idHorario <> :idExcluido)
            """)
    boolean existeTraslapePorAula(
            @Param("idAula") Long idAula,
            @Param("periodo") String periodo,
            @Param("idGrupo") Long idGrupo,
            @Param("dia") String dia,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin,
            @Param("idExcluido") Long idExcluido
    );
}
