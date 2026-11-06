package com.leonardo.escuela.services.inscripciones;
import com.leonardo.escuela.dto.inscripciones.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.InscripcionMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional @Slf4j
public class InscripcionServicesImpl implements InscripcionServices {
    private final InscripcionRepository inscripcionRepository;
    private final AlumnoRepository alumnoRepository;
    private final GrupoRepository grupoRepository;
    private final CalificacionRepository calificacionRepository;
    private final InscripcionMapper inscripcionMapper;
    @Override @Transactional(readOnly=true)
    public List<InscripcionResponse> Listar(){
        log.info("Consultando inscripciones");
        return inscripcionRepository.findAll().stream().map(inscripcionMapper::entidadAResponse).toList();
    }
    @Override @Transactional(readOnly=true)
    public InscripcionResponse obtenerPorId(Long id){
        log.info("Consultando inscripción {}",id);
        return inscripcionMapper.entidadAResponse(obtener(id));
    }
    @Override public InscripcionResponse registro(InscripcionRequest request){
        validarRequest(request);
        log.info("Registrando inscripción para alumno {} y grupo {}",request.idAlumno(),request.idGrupo());
        Alumno alumno=ServiceUtils.obtenerEntidadOException(alumnoRepository,request.idAlumno(),Alumno.class);
        Grupo grupo=ServiceUtils.obtenerEntidadOException(grupoRepository,request.idGrupo(),Grupo.class);
        validarUnica(request,null);
        return guardar(inscripcionMapper.requestAEntidad(request,alumno,grupo));
    }
    @Override public InscripcionResponse actualizar(InscripcionRequest request,Long id){
        validarRequest(request);
        log.info("Actualizando inscripción {}",id);
        Inscripcion inscripcion=obtener(id);
        Alumno alumno=ServiceUtils.obtenerEntidadOException(alumnoRepository,request.idAlumno(),Alumno.class);
        Grupo grupo=ServiceUtils.obtenerEntidadOException(grupoRepository,request.idGrupo(),Grupo.class);
        if (inscripcion.getAlumno().getIdAlumno().equals(request.idAlumno()) &&
            inscripcion.getGrupo().getIdGrupo().equals(request.idGrupo())) return inscripcionMapper.entidadAResponse(inscripcion);
        validarUnica(request,id);
        inscripcion.actualizar(alumno,grupo);
        return guardar(inscripcion);
    }
    @Override public void eliminar(Long id){
        log.info("Eliminando inscripción {}",id);
        Inscripcion inscripcion=obtener(id);
        if(calificacionRepository.existsByInscripcionIdInscripcion(id))
            throw new EntidadRelacionadaException("No se puede eliminar una inscripción con calificación");
        inscripcionRepository.delete(inscripcion);
        inscripcionRepository.flush();
        log.info("Inscripción {} eliminada",id);
    }
    private void validarRequest(InscripcionRequest r){
        if(r==null) throw new DatoInvalidoException("Los datos de la inscripción son requeridos");
    }
    private void validarUnica(InscripcionRequest r,Long id){
        boolean duplicado=id==null ? inscripcionRepository.existsByAlumnoIdAlumnoAndGrupoIdGrupo(r.idAlumno(),r.idGrupo())
          : inscripcionRepository.existsByAlumnoIdAlumnoAndGrupoIdGrupoAndIdInscripcionNot(r.idAlumno(),r.idGrupo(),id);
        if(duplicado) throw new ConflictoException("El alumno ya está inscrito en este grupo");
    }
    private Inscripcion obtener(Long id){return ServiceUtils.obtenerEntidadOException(inscripcionRepository,id,Inscripcion.class);}
    private InscripcionResponse guardar(Inscripcion e){
        Inscripcion guardada=inscripcionRepository.saveAndFlush(e);
        log.info("Inscripción {} guardada",guardada.getIdInscripcion());
        return inscripcionMapper.entidadAResponse(guardada);
    }
}
