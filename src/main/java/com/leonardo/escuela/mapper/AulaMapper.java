package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.aulas.AulaRequest;
import com.leonardo.escuela.dto.aulas.AulaResponse;
import com.leonardo.escuela.entities.Aula;
import org.springframework.stereotype.Component;

@Component
public class AulaMapper
        implements CommonMapper<AulaRequest, AulaResponse, Aula> {

    @Override
    public Aula requestAEntidad(AulaRequest request) {
        return request == null
                ? null
                : Aula.crear(
                request.nombre(),
                request.capacidad()
        );
    }

    @Override
    public AulaResponse entidadAResponse(Aula entidad) {
        return entidad == null ? null : new AulaResponse(
                entidad.getIdAula(),
                entidad.getNombre(),
                entidad.getCapacidad()
        );
    }
}

