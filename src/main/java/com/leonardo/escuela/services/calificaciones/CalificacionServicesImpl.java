package com.leonardo.escuela.services.calificaciones;
import com.leonardo.escuela.dto.calificaciones.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.CalificacionMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional @Slf4j
public class CalificacionServicesImpl implements CalificacionServices {
    private final CalificacionRepository calificacionRepository;
    private final InscripcionRepository inscripcionRepository;
    private final CalificacionMapper calificacionMapper;
    @Override @Transactional(readOnly=true)
    public List<CalificacionResponse> Listar(){
        log.info("Consultando calificaciones");
        return calificacionRepository.findAll().stream().map(calificacionMapper::entidadAResponse).toList();
    }
    @Override @Transactional(readOnly=true)
    public CalificacionResponse obtenerPorId(Long id){
        log.info("Consultando calificación {}",id);
        return calificacionMapper.entidadAResponse(obtener(id));
    }
    @Override public CalificacionResponse registro(CalificacionRequest request){
        validarRequest(request);
        log.info("Registrando calificación de inscripción {}",request.idInscripcion());
        Inscripcion inscripcion=ServiceUtils.obtenerEntidadOException(inscripcionRepository,request.idInscripcion(),Inscripcion.class);
        if(calificacionRepository.existsByInscripcionIdInscripcion(request.idInscripcion()))
            throw new ConflictoException("La inscripción ya tiene una calificación");
        return guardar(calificacionMapper.requestAEntidad(request,inscripcion));
    }
    @Override public CalificacionResponse actualizar(CalificacionRequest request,Long id){
        validarRequest(request);
        log.info("Actualizando calificación {}",id);
        Calificacion calificacion=obtener(id);
        if (!calificacion.getInscripcion().getIdInscripcion().equals(request.idInscripcion()))
            throw new DatoInvalidoException("No se puede cambiar la inscripción de una calificación");
        calificacion.actualizar(request.calificacion());
        return guardar(calificacion);
    }
    @Override public void eliminar(Long id){
        log.info("Eliminando calificación {}",id);
        calificacionRepository.delete(obtener(id));
        calificacionRepository.flush();
        log.info("Calificación {} eliminada",id);
    }
    private void validarRequest(CalificacionRequest r){
        if(r==null) throw new DatoInvalidoException("Los datos de la calificación son requeridos");
    }
    private Calificacion obtener(Long id){return ServiceUtils.obtenerEntidadOException(calificacionRepository,id,Calificacion.class);}
    private CalificacionResponse guardar(Calificacion e){
        Calificacion guardada=calificacionRepository.saveAndFlush(e);
        log.info("Calificación {} guardada",guardada.getIdCalificacion());
        return calificacionMapper.entidadAResponse(guardada);
    }
}
