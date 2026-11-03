package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.cursos.CursoRequest;
import com.leonardo.escuela.dto.cursos.CursoResponse;
import com.leonardo.escuela.dto.dato.DatosCurso;
import com.leonardo.escuela.entities.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper implements CommonMapper<CursoRequest, CursoResponse, Curso> {

    // CONVERTIR REQUEST EN ENTIDAD CON SUS VALIDACIONES
    @Override
    public Curso requestAEntidad(CursoRequest request) {
        return request == null ? null : Curso.crear(
                request.nombre(), request.descripcion(), request.creditos());
    }

    // CONVERTIR ENTIDAD EN RESPONSE
    @Override
    public CursoResponse entidadAResponse(Curso entidad) {
        return entidad == null ? null : new CursoResponse(
                entidad.getIdCurso(), entidad.getNombre(),
                entidad.getDescripcion(), entidad.getCreditos());
    }

    // CONSTRUIR DTO BREVE REUTILIZABLE POR OTROS MAPPERS
    public DatosCurso entidadADatosCurso(Curso entidad) {
        return entidad == null ? null : new DatosCurso(
                entidad.getNombre(), entidad.getDescripcion(), entidad.getCreditos());
    }
}
