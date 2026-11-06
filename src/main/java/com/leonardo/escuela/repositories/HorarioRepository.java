package com.leonardo.escuela.repositories;
import com.leonardo.escuela.entities.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface HorarioRepository extends JpaRepository<Horario,Long> {
    boolean existsByGrupoIdGrupo(Long idGrupo);
    List<Horario> findByDiaAndGrupoAulaIdAulaAndGrupoPeriodo(String dia,Long idAula,String periodo);
    List<Horario> findByDiaAndGrupoIdGrupo(String dia,Long idGrupo);
}
