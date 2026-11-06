package com.leonardo.escuela.services.curso;

import com.leonardo.escuela.dto.cursos.CursoRequest;
import com.leonardo.escuela.dto.cursos.CursoResponse;
import com.leonardo.escuela.entities.Curso;
import com.leonardo.escuela.exceptions.ConflictoException;
import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.exceptions.EntidadRelacionadaException;
import com.leonardo.escuela.mapper.CursoMapper;
import com.leonardo.escuela.repositories.CursoRepository;
import com.leonardo.escuela.repositories.GrupoRepository;
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
public class CursoServicesImpl implements CursoServices {

    private final CursoRepository cursoRepository;
    private final GrupoRepository grupoRepository;
    private final CursoMapper cursoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> Listar() {
        log.info("Consultando cursos");
        return cursoRepository.findAll().stream()
                .map(cursoMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse obtenerPorId(Long id) {
        log.info("Consultando curso con ID {}", id);
        return cursoMapper.entidadAResponse(obtenerCurso(id));
    }

    @Override
    public CursoResponse registro(CursoRequest request) {
        log.info("Registrando curso");
        validarRequest(request);

        Curso curso = cursoMapper.requestAEntidad(request);
        validarUnicidad(request, null);

        return guardarYResponder(curso);
    }

    @Override
    public CursoResponse actualizar(CursoRequest request, Long id) {
        log.info("Actualizando curso con ID {}", id);
        validarRequest(request);

        Curso curso = obtenerCurso(id);

        if (!curso.cambiaEnDatos(
                request.nombre(),
                request.descripcion(),
                request.creditos()))
            return cursoMapper.entidadAResponse(curso);

        validarUnicidad(request, id);

        curso.actualizar(
                request.nombre(),
                request.descripcion(),
                request.creditos()
        );

        return guardarYResponder(curso);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando curso con ID {}", id);
        Curso curso = obtenerCurso(id);

        if (grupoRepository.existsByCursoIdCurso(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el curso porque tiene grupos asignados"
            );

        cursoRepository.delete(curso);
        cursoRepository.flush();

        log.info("Curso eliminado correctamente con ID: {}", id);
    }

    private void validarRequest(CursoRequest request) {
        if (request == null)
            throw new DatoInvalidoException(
                    "Los datos del curso son requeridos"
            );
    }

    private void validarUnicidad(CursoRequest request, Long id) {
        String nombre = StringCustomUtils.limpiar(request.nombre());
        boolean duplicado = id == null
                ? cursoRepository.existsByNombreIgnoreCase(nombre)
                : cursoRepository.existsByNombreIgnoreCaseAndIdCursoNot(nombre, id);

        if (duplicado)
            throw new ConflictoException(
                    "Ya existe un curso con el nombre: " + nombre
            );
    }

    private Curso obtenerCurso(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                cursoRepository, id, Curso.class
        );
    }

    private CursoResponse guardarYResponder(Curso curso) {
        Curso guardado = cursoRepository.saveAndFlush(curso);
        log.info("Curso guardado con ID {}", guardado.getIdCurso());
        return cursoMapper.entidadAResponse(guardado);
    }
}