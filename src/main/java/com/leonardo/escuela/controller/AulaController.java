package com.leonardo.escuela.controller;


import com.leonardo.escuela.dto.aulas.AulaRequest;
import com.leonardo.escuela.dto.aulas.AulaResponse;
import com.leonardo.escuela.services.aula.AulaServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aulas")
@Tag(name = "API Aulas", description = "Metodos para la organizacion de las aulas")
public class AulaController extends CrudController<AulaRequest, AulaResponse, AulaServices> {

    public AulaController(AulaServices service){
        super(service);
    }

}
