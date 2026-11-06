package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.cursos.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.services.Fixtures;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CursoMapperTest {
    private final CursoMapper mapper = new CursoMapper();

    @Test
    void requestAEntidad_convierteDatosCorrectamente() {
        CursoRequest request = new CursoRequest("Java","Curso basico",5);
        Curso entidad = mapper.requestAEntidad(request);
        assertNotNull(entidad);
        assertEquals("Java", entidad.getNombre());
    }

    @Test
    void entidadAResponse_incluyeIdentificador() {
        var response = mapper.entidadAResponse(Fixtures.curso());
        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    @Test
    void parametrosNulos_noProducenRespuesta() {
        assertNull(mapper.requestAEntidad(null));
        assertNull(mapper.entidadAResponse(null));
    }

}
