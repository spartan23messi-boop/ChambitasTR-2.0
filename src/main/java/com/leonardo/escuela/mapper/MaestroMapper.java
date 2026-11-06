package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.datos.DatosCurso;
import com.leonardo.escuela.dto.maestros.MaestroRequest;
import com.leonardo.escuela.dto.maestros.MaestroResponse;
import com.leonardo.escuela.entities.Maestro;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class MaestroMapper implements CommonMapper<MaestroRequest, MaestroResponse, Maestro> {


    @Override
    public Maestro requestAEntidad(MaestroRequest request) {
        return request == null ? null : Maestro.crear(
                request.nombre(), request.apellidoPaterno(),
                request.apellidoMaterno(), request.email(), request.telefono());
    }

    @Override
    public MaestroResponse entidadAResponse(Maestro entidad) {
        return entidad == null ? null : new MaestroResponse(
                entidad.getIdMaestro(), entidad.obtenerNombreCompleto(),
                entidad.getEmail(), entidad.getTelefono(), entidadADatosCurso(entidad));
    }

    private List<DatosCurso> entidadADatosCurso(Maestro entidad) {
        if (entidad == null || entidad.getGrupos() == null) {
            return List.of();
        }
        return entidad.getGrupos().stream()
                .filter(Objects::nonNull)
                .map(grupo -> grupo.getCurso())
                .filter(Objects::nonNull)
                .distinct()
                .map(curso -> new DatosCurso(curso.getNombre(), curso.getDescripcion(), curso.getCreditos()))
                .toList();
    }
}
