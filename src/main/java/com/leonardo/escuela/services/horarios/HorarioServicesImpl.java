package com.leonardo.escuela.services.horarios;
import com.leonardo.escuela.dto.horarios.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.HorarioMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional @Slf4j
public class HorarioServicesImpl implements HorarioServices {
    private final HorarioRepository horarioRepository;
    private final GrupoRepository grupoRepository;
    private final HorarioMapper horarioMapper;
    @Override @Transactional(readOnly=true)
    public List<HorarioResponse> Listar() {
        log.info("Consultando horarios");
        return horarioRepository.findAll().stream().map(horarioMapper::entidadAResponse).toList();
    }
    @Override @Transactional(readOnly=true)
    public HorarioResponse obtenerPorId(Long id) {
        log.info("Consultando horario {}",id);
        return horarioMapper.entidadAResponse(obtenerHorario(id));
    }
    @Override public HorarioResponse registro(HorarioRequest request) {
        validarRequest(request);
        log.info("Iniciando registro de horario para grupo {}", request.idGrupo());
        Grupo grupo=obtenerGrupo(request.idGrupo());
        Horario horario=horarioMapper.requestAEntidad(request,grupo);
        validarTraslapes(horario,null);
        return guardar(horario);
    }
    @Override public HorarioResponse actualizar(HorarioRequest request,Long id) {
        validarRequest(request);
        log.info("Actualizando horario {}",id);
        Horario horario=obtenerHorario(id);
        Grupo grupo=obtenerGrupo(request.idGrupo());
        Horario actualizado=horarioMapper.requestAEntidad(request,grupo);
        validarTraslapes(actualizado,id);
        horario.actualizar(grupo,request.dia(),request.horaInicio(),request.horaFin());
        return guardar(horario);
    }
    @Override public void eliminar(Long id) {
        log.info("Eliminando horario {}",id);
        horarioRepository.delete(obtenerHorario(id));
        horarioRepository.flush();
        log.info("Horario {} eliminado",id);
    }
    private void validarTraslapes(Horario actual, Long idExcluido) {
        Grupo grupo = actual.getGrupo();

        if (horarioRepository.existeTraslapePorGrupo(
                grupo.getIdGrupo(), actual.getDia(), actual.getHoraInicio(),
                actual.getHoraFin(), idExcluido)) {
            throw new ConflictoException("El grupo ya tiene un horario traslapado");
        }

        if (horarioRepository.existeTraslapePorAula(
                grupo.getAula().getIdAula(), grupo.getPeriodo(), grupo.getIdGrupo(),
                actual.getDia(), actual.getHoraInicio(), actual.getHoraFin(), idExcluido)) {
            throw new ConflictoException(
                    "El aula ya está ocupada en ese día y horario para el período");
        }
    }

    private void validarRequest(HorarioRequest r) {
        if(r==null) throw new DatoInvalidoException("Los datos del horario son requeridos");
    }
    private Grupo obtenerGrupo(Long id) {return ServiceUtils.obtenerEntidadOException(grupoRepository,id,Grupo.class);}
    private Horario obtenerHorario(Long id) {return ServiceUtils.obtenerEntidadOException(horarioRepository,id,Horario.class);}
    private HorarioResponse guardar(Horario h) {
        Horario guardado=horarioRepository.saveAndFlush(h);
        log.info("Horario {} guardado",guardado.getIdHorario());
        return horarioMapper.entidadAResponse(guardado);
    }
}
