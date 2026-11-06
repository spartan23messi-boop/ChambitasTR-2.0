package com.leonardo.escuela.controller;

import com.leonardo.escuela.dto.alumnos.AlumnoRequest;
import com.leonardo.escuela.dto.alumnos.AlumnoResponse;
import com.leonardo.escuela.services.alumno.AlumnoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alumnos")
@Tag(name = "API Alumno", description = "Métodos para gestión de alumnos")
public class AlumnoController extends CrudController<AlumnoRequest, AlumnoResponse, AlumnoService> {

    public AlumnoController(AlumnoService service) {
        super(service);
    }
}
