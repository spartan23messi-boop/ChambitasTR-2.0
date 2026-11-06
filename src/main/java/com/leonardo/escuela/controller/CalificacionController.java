package com.leonardo.escuela.controller;
import com.leonardo.escuela.dto.calificaciones.*;
import com.leonardo.escuela.services.calificaciones.CalificacionServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/calificaciones")
@Tag(name="API Calificaciones",description="Administración de calificaciones")
public class CalificacionController extends CrudController<CalificacionRequest,CalificacionResponse,CalificacionServices> {
    public CalificacionController(CalificacionServices service) {super(service);}
}
