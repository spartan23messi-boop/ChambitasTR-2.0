package com.leonardo.escuela.controller;
import com.leonardo.escuela.dto.horarios.*;
import com.leonardo.escuela.services.horarios.HorarioServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/horarios")
@Tag(name="API Horarios",description="Administración de horarios")
public class HorarioController extends CrudController<HorarioRequest,HorarioResponse,HorarioServices> {
    public HorarioController(HorarioServices service) {super(service);}
}
