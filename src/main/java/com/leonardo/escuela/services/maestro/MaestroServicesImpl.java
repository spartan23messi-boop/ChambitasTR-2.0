package com.leonardo.escuela.services.maestro;

import com.leonardo.escuela.dto.maestros.MaestroRequest;
import com.leonardo.escuela.dto.maestros.MaestroResponse;
import com.leonardo.escuela.entities.Maestro;
import com.leonardo.escuela.exceptions.ConflictoException;
import com.leonardo.escuela.exceptions.EntidadRelacionadaException;
import com.leonardo.escuela.mapper.MaestroMapper;
import com.leonardo.escuela.repositories.GrupoRepository;
import com.leonardo.escuela.repositories.MaestroRepository;

import com.leonardo.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.ConnectException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // en caso de que algún proceso falle hace un roll back para que los datos no queden incoherentes
@Slf4j//Simple loging facade for java-> crea un logger en la consola
public class MaestroServicesImpl implements MestroService {

    // DEPENDENCIAS
    private final MaestroRepository maestroRepository;
    private final GrupoRepository grupoRepository;
    private final MaestroMapper maestroMapper;

    // LISTAR MAESTROS
    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> Listar() {

        return maestroRepository.findAll()
                .stream()
                .map(maestroMapper::entidadAResponse)
                .toList();
    }

    @Override
    public MaestroResponse obtenerPorId(Long id) {
        return maestroMapper.entidadAResponse(obtenerMaestro(id));
    }

    @Override
    public MaestroResponse registro(MaestroRequest request) {

        log.info("Registrando nuevo maestro...");

        validarDatosUnicos(request);

        Maestro maestro = maestroMapper.requestAEntidad(request);

        maestroRepository.saveAndFlush(maestro);

        log.info("Nuevo maestro {} registrado",maestro.obtenerNombreCompleto());

        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    public MaestroResponse actualizar(MaestroRequest request, Long id) {
        Maestro maestro= obtenerMaestro(id);
        log.info("Actualizando maestro con id: {}",id);
        validarCambiosUnicos(request,id);
        maestro.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono());

        maestroRepository.saveAndFlush(maestro);
        log.info("Maestro {} actualizando", maestro.obtenerNombreCompleto());
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

        log.info("Maestro {} eliminado",maestro.obtenerNombreCompleto());

    }


    private Maestro obtenerMaestro(Long id){
        return ServiceUtils.obtenerEntidadOException(maestroRepository,id,Maestro.class);
    }

    public void validarDatosUnicos(MaestroRequest request){
        log.info("validando email unico....");

        if(maestroRepository.existsByEmailIgnoreCase(request.email().trim()))
            throw new ConflictoException("Ya existe un maestro registra con el email: "+request.email());

        log.info("Validando telefono unico....");

        if(maestroRepository.existsByTelefono(request.telefono().trim()))
            throw new ConflictoException("Ya existe un maestro regitrado con el telefono: "+request.telefono());
    }
    public void validarCambiosUnicos(MaestroRequest request, Long id) {

        log.info("Validando cambio en email único...");

        if (maestroRepository.existsByEmailIgnoreCaseAndIdMaestroNot(request.email().trim(), id))
            throw new ConflictoException("Ya existe un maestro registrado con el email: " + request.email());

        log.info("Validando cambio en teléfono único...");

        if (maestroRepository.existsByTelefonoAndIdMaestroNot(request.telefono().trim(), id))
            throw new ConflictoException("Ya existe un maestro registrado con el teléfono: " + request.telefono());
    }


}