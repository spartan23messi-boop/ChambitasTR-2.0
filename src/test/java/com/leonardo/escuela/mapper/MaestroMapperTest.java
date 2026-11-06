package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.maestros.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.services.Fixtures;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MaestroMapperTest {
    private final MaestroMapper mapper = new MaestroMapper();

    @Test
    void requestAEntidad_convierteDatosCorrectamente() {
        MaestroRequest request = new MaestroRequest("Laura","Garcia","Lopez","laura@test.com","2223456789");
        Maestro entidad = mapper.requestAEntidad(request);
        assertNotNull(entidad);
        assertEquals("Laura", entidad.getNombre());
    }

    @Test
    void entidadAResponse_incluyeIdentificador() {
        var response = mapper.entidadAResponse(Fixtures.maestro());
        assertNotNull(response);
        assertEquals(2L, response.id());
    }

    @Test
    void parametrosNulos_noProducenRespuesta() {
        assertNull(mapper.requestAEntidad(null));
        assertNull(mapper.entidadAResponse(null));
    }

}
