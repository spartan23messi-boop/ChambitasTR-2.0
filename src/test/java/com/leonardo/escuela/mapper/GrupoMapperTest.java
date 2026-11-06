package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.grupos.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.services.Fixtures;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class GrupoMapperTest {
    private final GrupoMapper mapper = new GrupoMapper();

    @Test
    void requestAEntidad_convierteDatosCorrectamente() {
        GrupoRequest request = new GrupoRequest(1L,2L,3L,"2026-01");
        Grupo entidad = mapper.requestAEntidad(request,Fixtures.curso(),Fixtures.maestro(),Fixtures.aula());
        assertNotNull(entidad);
        assertEquals("2026-01", entidad.getPeriodo());
    }

    @Test
    void entidadAResponse_incluyeIdentificador() {
        var response = mapper.entidadAResponse(Fixtures.grupo());
        assertNotNull(response);
        assertEquals(1L, response.idGrupo());
    }

    @Test
    void entidadNula_noProduceResponse() {
        assertNull(mapper.entidadAResponse(null));
    }

}
