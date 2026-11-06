package com.leonardo.escuela.controller;

import com.leonardo.escuela.dto.cursos.CursoRequest;
import com.leonardo.escuela.dto.cursos.CursoResponse;
import com.leonardo.escuela.services.curso.CursoServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos")
@Tag(name = "API Cursos", description = "Organización de los cursos")
public class CursoController
        extends CrudController<CursoRequest, CursoResponse, CursoServices> {

    public CursoController(CursoServices services) {
        super(services);
    }
}