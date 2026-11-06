package com.leonardo.escuela.services.inscripciones;

import com.leonardo.escuela.dto.inscripciones.InscripcionRequest;
import com.leonardo.escuela.dto.inscripciones.InscripcionResponse;
import com.leonardo.escuela.entities.Alumno;
import com.leonardo.escuela.entities.Grupo;
import com.leonardo.escuela.entities.Inscripcion;
import com.leonardo.escuela.exceptions.ConflictoException;
import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.exceptions.EntidadRelacionadaException;
import com.leonardo.escuela.mapper.InscripcionMapper;
import com.leonardo.escuela.repositories.AlumnoRepository;
import com.leonardo.escuela.repositories.CalificacionRepository;
import com.leonardo.escuela.repositories.GrupoRepository;
import com.leonardo.escuela.repositories.InscripcionRepository;
import com.leonardo.escuela.utils.ServiceUtils;
import com.leonardo.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InscripcionServicesImpl implements InscripcionServices {

    private final InscripcionRepository inscripcionRepository;
    private final AlumnoRepository alumnoRepository;
    private final GrupoRepository grupoRepository;
    private final CalificacionRepository calificacionRepository;
    private final InscripcionMapper inscripcionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponse> Listar() {
        log.info("Consultando inscripciones");
        return inscripcionRepository.findAll().stream()
                .map(inscripcionMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InscripcionResponse obtenerPorId(Long id) {
        log.info("Consultando inscripción {}", id);
        return inscripcionMapper.entidadAResponse(obtener(id));
    }

    @Override
    public InscripcionResponse registro(InscripcionRequest request) {
        validarRequest(request);
        log.info("Registrando inscripción para alumno {} y grupo {}", request.idAlumno(), request.idGrupo());

        Alumno alumno = ServiceUtils.obtenerAlumno(alumnoRepository, request.idAlumno());
        Grupo grupo = obtenerGrupo(request.idGrupo());
        validarUnicidad(request, null);

        return guardar(inscripcionMapper.requestAEntidad(request, alumno, grupo));
    }

    @Override
    public InscripcionResponse actualizar(InscripcionRequest request, Long id) {
        validarRequest(request);
        log.info("Actualizando inscripción {}", id);

        Inscripcion inscripcion = obtener(id);
        Alumno alumno = ServiceUtils.obtenerAlumno(alumnoRepository, request.idAlumno());
        Grupo grupo = obtenerGrupo(request.idGrupo());

        if (Objects.equals(inscripcion.getAlumno().getIdAlumno(), request.idAlumno())
                && Objects.equals(inscripcion.getGrupo().getIdGrupo(), request.idGrupo())) {
            return inscripcionMapper.entidadAResponse(inscripcion);
        }

        validarUnicidad(request, id);
        inscripcion.actualizar(alumno, grupo);
        return guardar(inscripcion);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando inscripción {}", id);
        Inscripcion inscripcion = obtener(id);

        if (calificacionRepository.existsByInscripcionIdInscripcion(id)) {
            throw new EntidadRelacionadaException("No se puede eliminar una inscripción con calificación");
        }

        inscripcionRepository.delete(inscripcion);
        inscripcionRepository.flush();
        log.info("Inscripción {} eliminada", id);
    }

    private void validarRequest(InscripcionRequest request) {
        if (request == null) {
            throw new DatoInvalidoException("Los datos de la inscripción son requeridos");
        }
    }

    private void validarUnicidad(InscripcionRequest request, Long id) {
        boolean duplicado = id == null
                ? inscripcionRepository.existsByAlumnoIdAlumnoAndGrupoIdGrupo(
                        request.idAlumno(), request.idGrupo())
                : inscripcionRepository.existsByAlumnoIdAlumnoAndGrupoIdGrupoAndIdInscripcionNot(
                        request.idAlumno(), request.idGrupo(), id);

        if (duplicado) {
            throw new ConflictoException("El alumno ya está inscrito en este grupo");
        }
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupo.class);
    }

    private Inscripcion obtener(Long id) {
        return ServiceUtils.obtenerEntidadOException(inscripcionRepository, id, Inscripcion.class);
    }

    private InscripcionResponse guardar(Inscripcion inscripcion) {
        Inscripcion guardada = inscripcionRepository.saveAndFlush(inscripcion);
        log.info("Inscripción {} guardada", guardada.getIdInscripcion());
        return inscripcionMapper.entidadAResponse(guardada);
    }
}
