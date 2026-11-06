package com.leonardo.escuela.controller;
import com.leonardo.escuela.dto.grupos.*;
import com.leonardo.escuela.services.grupo.GrupoServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/grupos")
@Tag(name="API Grupos", description="Administración de grupos")
public class GrupoController extends CrudController<GrupoRequest,GrupoResponse,GrupoServices> {
    public GrupoController(GrupoServices service) { super(service); }
}
