package com.leonardo.escuela.services.grupo;

import com.leonardo.escuela.dto.grupos.GrupoRequest;
import com.leonardo.escuela.dto.grupos.GrupoResponse;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.ConflictoException;
import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.exceptions.EntidadRelacionadaException;
import com.leonardo.escuela.mapper.GrupoMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.utils.ServiceUtils;
import com.leonardo.escuela.utils.StringCustomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GrupoServicesImpl implements GrupoServices {

    private final GrupoRepository grupoRepository;
    private final CursoRepository cursoRepository;
    private final MaestroRepository maestroRepository;
    private final AulaRepository aulaRepository;
    private final InscripcionRepository inscripcionRepository;
    private final HorarioRepository horarioRepository;
    private final GrupoMapper grupoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponse> Listar() {
        log.info("Consultando grupos");
        return grupoRepository.findAll().stream()
                .map(grupoMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponse obtenerPorId(Long id) {
        log.info("Consultando grupo con ID {}", id);
        return grupoMapper.entidadAResponse(obtenerGrupo(id));
    }

    @Override
    public GrupoResponse registro(GrupoRequest request) {
        log.info("Registrando grupo");
        validarRequest(request);

        Curso curso = obtenerCurso(request.idCurso());
        Maestro maestro = obtenerMaestro(request.idMaestro());
        Aula aula = obtenerAula(request.idAula());

        validarGrupoUnico(request, null);

        return guardarYResponder(
                grupoMapper.requestAEntidad(request, curso, maestro, aula)
        );
    }

    @Override
    public GrupoResponse actualizar(GrupoRequest request, Long id) {
        log.info("Actualizando grupo con ID {}", id);
        validarRequest(request);

        Grupo grupo = obtenerGrupo(id);
        Curso curso = obtenerCurso(request.idCurso());
        Maestro maestro = obtenerMaestro(request.idMaestro());
        Aula aula = obtenerAula(request.idAula());

        if (!grupo.cambiaEnDatos(
                curso, maestro, aula, request.periodo()))
            return grupoMapper.entidadAResponse(grupo);

        validarGrupoUnico(request, id);
        grupo.actualizar(curso, maestro, aula, request.periodo());

        return guardarYResponder(grupo);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando grupo con ID {}", id);
        Grupo grupo = obtenerGrupo(id);

        if (inscripcionRepository.existsByGrupoIdGrupo(id)
                || horarioRepository.existsByGrupoIdGrupo(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el grupo porque tiene inscripciones u horarios asignados"
            );

        grupoRepository.delete(grupo);
        grupoRepository.flush();

        log.info("Grupo eliminado correctamente con ID: {}", id);
    }

    private void validarRequest(GrupoRequest request) {
        if (request == null)
            throw new DatoInvalidoException(
                    "Los datos del grupo son requeridos"
            );
    }

    private void validarGrupoUnico(GrupoRequest request, Long id) {
        String periodo = StringCustomUtils.limpiar(request.periodo());

        boolean duplicado = id == null
                ? grupoRepository.existsByCursoIdCursoAndMaestroIdMaestroAndAulaIdAulaAndPeriodo(
                request.idCurso(),
                request.idMaestro(),
                request.idAula(),
                periodo
        )
                : grupoRepository.existsByCursoIdCursoAndMaestroIdMaestroAndAulaIdAulaAndPeriodoAndIdGrupoNot(
                request.idCurso(),
                request.idMaestro(),
                request.idAula(),
                periodo,
                id
        );

        if (duplicado)
            throw new ConflictoException(
                    "Ya existe un grupo con el mismo curso, maestro, aula y período"
            );
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                grupoRepository, id, Grupo.class
        );
    }

    private Curso obtenerCurso(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                cursoRepository, id, Curso.class
        );
    }

    private Maestro obtenerMaestro(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                maestroRepository, id, Maestro.class
        );
    }

    private Aula obtenerAula(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                aulaRepository, id, Aula.class
        );
    }

    private GrupoResponse guardarYResponder(Grupo grupo) {
        Grupo guardado = grupoRepository.saveAndFlush(grupo);
        log.info("Grupo guardado con ID {}", guardado.getIdGrupo());
        return grupoMapper.entidadAResponse(guardado);
    }
}