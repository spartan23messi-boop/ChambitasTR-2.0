package com.leonardo.escuela.controller;
import com.leonardo.escuela.dto.inscripciones.*;
import com.leonardo.escuela.services.inscripciones.InscripcionServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/inscripciones")
@Tag(name="API Inscripciones",description="Administración de inscripciones")
public class InscripcionController extends CrudController<InscripcionRequest,InscripcionResponse,InscripcionServices> {
    public InscripcionController(InscripcionServices service) {super(service);}
}
