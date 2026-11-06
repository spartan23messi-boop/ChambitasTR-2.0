package com.leonardo.escuela.services.alumno;

import com.leonardo.escuela.dto.alumnos.AlumnoRequest;
import com.leonardo.escuela.dto.alumnos.AlumnoResponse;
import com.leonardo.escuela.entities.Alumno;
import com.leonardo.escuela.exceptions.ConflictoException;
import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.exceptions.EntidadRelacionadaException;
import com.leonardo.escuela.mapper.AlumnoMapper;
import com.leonardo.escuela.repositories.AlumnoRepository;
import com.leonardo.escuela.repositories.InscripcionRepository;
import com.leonardo.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AlumnoServicesImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final AlumnoMapper alumnoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponse> Listar() {
        log.info("Consultando alumnos");
        return alumnoRepository.findAll().stream()
                .map(alumnoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponse obtenerPorId(Long id) {
        log.info("Consultando alumno con ID {}", id);
        return alumnoMapper.entidadAResponse(ServiceUtils.obtenerAlumno(alumnoRepository, id));
    }

    @Override
    public AlumnoResponse registro(AlumnoRequest request) {
        log.info("Registrando alumno");
        validarRequest(request);
        Alumno alumno = alumnoMapper.requestAEntidad(request);
        generarDatosAcademicos(alumno);
        validarDatosUnicos(alumno, null);
        return guardarYResponder(alumno);
    }

    @Override
    public AlumnoResponse actualizar(AlumnoRequest request, Long id) {
        log.info("Actualizando alumno con ID {}", id);
        validarRequest(request);
        Alumno alumno = ServiceUtils.obtenerAlumno(alumnoRepository, id);
        if (!alumno.cambiaEnDatos(request.nombre(), request.apellidoPaterno(), request.apellidoMaterno()))
            return alumnoMapper.entidadAResponse(alumno);
        alumno.actualizar(request.nombre(), request.apellidoPaterno(), request.apellidoMaterno());
        generarDatosAcademicos(alumno);
        validarDatosUnicos(alumno, id);
        return guardarYResponder(alumno);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando alumno con ID {}", id);
        Alumno alumno = ServiceUtils.obtenerAlumno(alumnoRepository, id);
        if (inscripcionRepository.existsByAlumnoIdAlumno(id))
            throw new EntidadRelacionadaException("No se puede eliminar el alumno porque tiene inscripciones asignadas");
        alumnoRepository.delete(alumno);
        alumnoRepository.flush();
        log.info("Alumno eliminado correctamente con ID: {}", id);
    }

    private void validarRequest(AlumnoRequest request) {
        if (request == null)
            throw new DatoInvalidoException("Los datos del alumno son requeridos");
    }

    private void generarDatosAcademicos(Alumno alumno) {
        String nombre = alumno.getNombre();
        String paterno = alumno.getApellidoPaterno();
        String materno = alumno.getApellidoMaterno();
        String matricula = alumnoRepository.generarMatricula(nombre, paterno, materno);
        String email = alumnoRepository.generarEmail(nombre, paterno, materno);
        alumno.asignarDatosAcademicos(email, matricula);
    }

    private void validarDatosUnicos(Alumno alumno, Long id) {
        boolean matriculaDuplicada = id == null
                ? alumnoRepository.existsByMatricula(alumno.getMatricula())
                : alumnoRepository.existsByMatriculaAndIdAlumnoNot(alumno.getMatricula(), id);
        boolean emailDuplicado = id == null
                ? alumnoRepository.existsByEmailIgnoreCase(alumno.getEmail())
                : alumnoRepository.existsByEmailIgnoreCaseAndIdAlumnoNot(alumno.getEmail(), id);
        if (matriculaDuplicada || emailDuplicado)
            throw new ConflictoException("La matrícula o el correo generado ya existe");
    }

    private AlumnoResponse guardarYResponder(Alumno alumno) {
        Alumno guardado = alumnoRepository.saveAndFlush(alumno);
        log.info("Alumno guardado con ID {}", guardado.getIdAlumno());
        return alumnoMapper.entidadAResponse(guardado);
    }
}
