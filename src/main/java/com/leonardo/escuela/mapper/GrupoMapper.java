package com.leonardo.escuela.mapper;
import com.leonardo.escuela.dto.grupos.*;
import com.leonardo.escuela.entities.*;
import org.springframework.stereotype.Component;
@Component
public class GrupoMapper {
    public Grupo requestAEntidad(GrupoRequest request, Curso curso, Maestro maestro, Aula aula) {
        return request == null ? null : Grupo.crear(curso, maestro, aula, request.periodo());
    }
    public GrupoResponse entidadAResponse(Grupo entidad) {
        return entidad == null ? null : new GrupoResponse(
            entidad.getIdGrupo(), entidad.getCurso().getIdCurso(),
            entidad.getMaestro().getIdMaestro(), entidad.getAula().getIdAula(),
            entidad.getPeriodo(), entidad.getHorarios().stream()
                .map(h -> new com.leonardo.escuela.dto.horarios.HorarioResponse(
                    h.getIdHorario(), h.getGrupo().getIdGrupo(), h.getDia(), h.getHoraInicio(), h.getHoraFin()))
                .toList());
    }
}
