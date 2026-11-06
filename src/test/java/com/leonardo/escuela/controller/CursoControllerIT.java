package com.leonardo.escuela.controller;

import com.leonardo.escuela.services.curso.CursoServices;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.dto.cursos.CursoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CursoControllerIT {
    private final CursoServices service = mock(CursoServices.class);
    private final MockMvc mvc = crearMvc();

    private MockMvc crearMvc() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        return MockMvcBuilders.standaloneSetup(new CursoController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void registrar_datosValidos_registraCurso() throws Exception {
        CursoResponse respuesta = new CursoResponse(1L, "Java", "Curso basico", 5);
        when(service.registro(any())).thenReturn(respuesta);
        mvc.perform(post("/api/cursos").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Java\",\"descripcion\":\"Curso basico\",\"creditos\":5}")).andExpect(status().is(201)).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void registrar_nombreDuplicado_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).registro(any());
        mvc.perform(post("/api/cursos").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Java\",\"descripcion\":\"Curso basico\",\"creditos\":5}")).andExpect(status().is(409));
    }

    @Test
    void obtenerPorId_cursoExistente_retornaCurso() throws Exception {
        CursoResponse respuesta = new CursoResponse(1L, "Java", "Curso basico", 5);
        when(service.obtenerPorId(anyLong())).thenReturn(respuesta);
        mvc.perform(get("/api/cursos/1")).andExpect(status().is(200)).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).obtenerPorId(anyLong());
        mvc.perform(get("/api/cursos/1")).andExpect(status().is(404));
    }

    @Test
    void listar_conCursos_retornaLista() throws Exception {
        CursoResponse respuesta = new CursoResponse(1L, "Java", "Curso basico", 5);
        when(service.Listar()).thenReturn(java.util.List.of(respuesta));
        mvc.perform(get("/api/cursos")).andExpect(status().is(200)).andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() throws Exception {
        doThrow(new RuntimeException("Falló el repositorio")).when(service).Listar();
        mvc.perform(get("/api/cursos")).andExpect(status().is(500));
    }

    @Test
    void actualizar_mismoNombrePropio_noLanzaConflicto() throws Exception {
        CursoResponse respuesta = new CursoResponse(1L, "Java", "Curso basico", 5);
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/cursos/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Java\",\"descripcion\":\"Curso basico\",\"creditos\":5}")).andExpect(status().is(200)).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_nombreDeOtroCurso_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).actualizar(any(), anyLong());
        mvc.perform(put("/api/cursos/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Java\",\"descripcion\":\"Curso basico\",\"creditos\":5}")).andExpect(status().is(409));
    }

    @Test
    void eliminar_sinGrupos_eliminaCurso() throws Exception {
        mvc.perform(delete("/api/cursos/1")).andExpect(status().is(204));
    }

    @Test
    void eliminar_conGrupos_lanzaEntidadRelacionadaException() throws Exception {
        doThrow(new EntidadRelacionadaException("Entidad relacionada")).when(service).eliminar(anyLong());
        mvc.perform(delete("/api/cursos/1")).andExpect(status().is(409));
    }

}
