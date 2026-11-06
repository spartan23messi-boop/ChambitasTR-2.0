package com.leonardo.escuela.mapper;
import com.leonardo.escuela.dto.horarios.*;
import com.leonardo.escuela.entities.*;
import org.springframework.stereotype.Component;
@Component
public class HorarioMapper {
    public Horario requestAEntidad(HorarioRequest request, Grupo grupo) {
        return request == null ? null : Horario.crear(grupo, request.dia(), request.horaInicio(), request.horaFin());
    }
    public HorarioResponse entidadAResponse(Horario entidad) {
        return entidad == null ? null : new HorarioResponse(entidad.getIdHorario(), entidad.getGrupo().getIdGrupo(), entidad.getDia(), entidad.getHoraInicio(), entidad.getHoraFin());
    }
}
