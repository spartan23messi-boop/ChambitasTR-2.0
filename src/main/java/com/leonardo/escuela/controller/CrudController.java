
package com.leonardo.escuela.controller;

import com.leonardo.escuela.docs.ProblemaDoc;
import com.leonardo.escuela.services.CrudServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

@RequiredArgsConstructor
@Validated
@ApiResponse(
        responseCode = "400",
        description = "Datos o parámetros inválidos",
        content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)
        )
)
@ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)
        )
)
public class CrudController<RQ, RS, S extends CrudServices<RQ, RS>> {

    protected final S service;

    // LISTAR
    @GetMapping
    @Operation(summary = "Listar registros")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<RS>> listar() {

        return ResponseEntity.ok(service.Listar());
    }

    // OBTENER REGISTRO POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener registro por ID")
    @ApiResponse(responseCode = "200", description = "Registro encontrado")
    @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    public ResponseEntity<RS> obtenerPorId(
            @PathVariable @Positive Long id) {

        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // REGISTRAR
    @PostMapping
    @Operation(summary = "Registrar nuevo registro")
    @ApiResponse(responseCode = "201", description = "Registro creado")
    public ResponseEntity<RS> registrar(
            @Valid @RequestBody RQ request) {

        RS respuesta = service.registro(request);

        return ResponseEntity.status(201).body(respuesta);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar registro")
    @ApiResponse(responseCode = "200", description = "Registro actualizado")
    @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    public ResponseEntity<RS> actualizar(
            @PathVariable @Positive Long id,
            @Valid @RequestBody RQ request) {

        return ResponseEntity.ok(service.actualizar(request, id));
    }

    // ELIMINAR REGISTRO
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro")
    @ApiResponse(responseCode = "204", description = "Registro eliminado")
    @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive Long id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
