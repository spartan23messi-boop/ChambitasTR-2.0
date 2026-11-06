package com.leonardo.escuela.services.maestro;

import com.leonardo.escuela.dto.maestros.MaestroRequest;
import com.leonardo.escuela.dto.maestros.MaestroResponse;
import com.leonardo.escuela.entities.Maestro;
import com.leonardo.escuela.exceptions.ConflictoException;
import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.exceptions.EntidadRelacionadaException;
import com.leonardo.escuela.mapper.MaestroMapper;
import com.leonardo.escuela.repositories.GrupoRepository;
import com.leonardo.escuela.repositories.MaestroRepository;

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
public class MaestroServicesImpl implements MaestroService {

    private final MaestroRepository maestroRepository;
    private final GrupoRepository grupoRepository;
    private final MaestroMapper maestroMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> Listar() {
        log.info("Consultando maestros");

        return maestroRepository.findAll()
                .stream()
                .map(maestroMapper::entidadAResponse)
                .toList();
    }

    @Override
    public MaestroResponse obtenerPorId(Long id) {
        log.info("Consultando maestro con ID {}", id);
        return maestroMapper.entidadAResponse(obtenerMaestro(id));
    }

    @Override
    public MaestroResponse registro(MaestroRequest request) {

        log.info("Registrando nuevo maestro...");

        validarRequest(request);
        validarDatosUnicos(request);

        Maestro maestro = maestroMapper.requestAEntidad(request);

        maestroRepository.saveAndFlush(maestro);

        log.info("Maestro registrado con ID {}", maestro.getIdMaestro());

        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    public MaestroResponse actualizar(MaestroRequest request, Long id) {
        validarRequest(request);
        log.info("Actualizando maestro con ID {}", id);
        Maestro maestro = obtenerMaestro(id);
        validarCambiosUnicos(request,id);
        maestro.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono());

        maestroRepository.saveAndFlush(maestro);
        log.info("Maestro actualizado con ID {}", id);
        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    public void eliminar(Long id) {

        Maestro maestro= obtenerMaestro(id);

        log.info("Eliminando maestro con id: {}",id);

        if(grupoRepository.existsByMaestroIdMaestro(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el maestro ya que tiene grupos asignados"
            );

        maestroRepository.delete(maestro);
        maestroRepository.flush();

        log.info("Maestro eliminado con ID {}", id);

    }


    private void validarRequest(MaestroRequest request) {
        if (request == null) throw new DatoInvalidoException("Los datos del maestro son requeridos");
    }

    private Maestro obtenerMaestro(Long id){
        return ServiceUtils.obtenerEntidadOException(maestroRepository,id,Maestro.class);
    }

    private void validarDatosUnicos(MaestroRequest request){
        log.info("validando email unico....");

        if(maestroRepository.existsByEmailIgnoreCase(request.email().trim()))
            throw new ConflictoException("Ya existe un maestro registra con el email: "+request.email());

        log.info("Validando telefono unico....");

        if(maestroRepository.existsByTelefono(request.telefono().trim()))
            throw new ConflictoException("Ya existe un maestro regitrado con el telefono: "+request.telefono());
    }
    private void validarCambiosUnicos(MaestroRequest request, Long id) {

        log.info("Validando cambio en email único...");

        if (maestroRepository.existsByEmailIgnoreCaseAndIdMaestroNot(request.email().trim(), id))
            throw new ConflictoException("Ya existe un maestro registrado con el email: " + request.email());

        log.info("Validando cambio en teléfono único...");

        if (maestroRepository.existsByTelefonoAndIdMaestroNot(request.telefono().trim(), id))
            throw new ConflictoException("Ya existe un maestro registrado con el teléfono: " + request.telefono());
    }


}