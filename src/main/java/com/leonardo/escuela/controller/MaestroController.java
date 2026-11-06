package com.leonardo.escuela.controller;

import com.leonardo.escuela.dto.maestros.MaestroRequest;
import com.leonardo.escuela.dto.maestros.MaestroResponse;
import com.leonardo.escuela.services.maestro.MaestroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maestros")
@Tag(name = "API Maestro", description = "Métodos para gestión de maestros")
public class MaestroController extends CrudController<MaestroRequest, MaestroResponse, MaestroService> {

    public MaestroController(MaestroService service) {
        super(service);
    }
}
