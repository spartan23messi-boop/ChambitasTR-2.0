package com.leonardo.escuela.services.aula;

import com.leonardo.escuela.dto.aulas.AulaRequest;
import com.leonardo.escuela.dto.aulas.AulaResponse;
import com.leonardo.escuela.entities.Aula;
import com.leonardo.escuela.exceptions.ConflictoException;
import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.exceptions.EntidadRelacionadaException;
import com.leonardo.escuela.mapper.AulaMapper;
import com.leonardo.escuela.repositories.AulaRepository;
import com.leonardo.escuela.repositories.GrupoRepository;
import com.leonardo.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AulaServicesImpl implements AulaServices {

    private final AulaRepository aulaRepository;
    private final GrupoRepository grupoRepository;
    private final AulaMapper aulaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AulaResponse> Listar(){
        log.info("Consultando aulas");
        return aulaRepository.findAll().stream()
                .map(aulaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AulaResponse obtenerPorId(Long id) {
        log.info("Consultando aula con ID {}", id);
        return aulaMapper.entidadAResponse(obtenerAula(id));
    }

    @Override
    public AulaResponse registro(AulaRequest request) {
        log.info("Registrando aula");
        validarRequest(request);
        Aula aula = aulaMapper.requestAEntidad(request);
        validarNombreUnico(aula.getNombre(), null);
        return guardarYResponder(aula);
    }

    @Override
    public AulaResponse actualizar(AulaRequest request, Long id) {
        log.info("Actualizando aula con ID {}", id);
        validarRequest(request);
        Aula aula = obtenerAula(id);

        if (!aula.cambiaEnDatos(request.nombre(), request.capacidad()))
            return aulaMapper.entidadAResponse(aula);

        validarNombreUnico(request.nombre().trim(), id);
        aula.actualizar(request.nombre(), request.capacidad());

        return guardarYResponder(aula);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando aula con ID {}", id);
        Aula aula = obtenerAula(id);

        if (grupoRepository.existsByAulaIdAula(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el aula porque tiene grupos asignados"
            );

        aulaRepository.delete(aula);
        aulaRepository.flush();
        log.info("Aula eliminada correctamente con ID: {}", id);

    }

    private void validarRequest(AulaRequest request) {
        if (request == null)
            throw new DatoInvalidoException(
                    "Los datos del aula son requeridos"
            );
    }

    private void validarNombreUnico(String nombre, Long id) {
        boolean duplicado = id == null
                ? aulaRepository.existsByNombreIgnoreCase(nombre)
                : aulaRepository.existsByNombreIgnoreCaseAndIdAulaNot(nombre, id);

        if (duplicado)
            throw new ConflictoException(
                    "Ya existe un aula con el nombre: " + nombre
            );
    }

    private Aula obtenerAula(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                aulaRepository, id, Aula.class
        );
    }

    private AulaResponse guardarYResponder(Aula aula) {
        Aula guardado = aulaRepository.saveAndFlush(aula);
        log.info("Aula guardado con ID {}", guardado.getIdAula());
        return aulaMapper.entidadAResponse(guardado);
    }
}





