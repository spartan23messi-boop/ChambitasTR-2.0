package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.aulas.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.services.Fixtures;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class AulaMapperTest {
    private final AulaMapper mapper = new AulaMapper();

    @Test
    void requestAEntidad_convierteDatosCorrectamente() {
        AulaRequest request = new AulaRequest("Laboratorio",30);
        Aula entidad = mapper.requestAEntidad(request);
        assertNotNull(entidad);
        assertEquals("Laboratorio", entidad.getNombre());
    }

    @Test
    void entidadAResponse_incluyeIdentificador() {
        var response = mapper.entidadAResponse(Fixtures.aula());
        assertNotNull(response);
        assertEquals(3L, response.idAula());
    }

    @Test
    void parametrosNulos_noProducenRespuesta() {
        assertNull(mapper.requestAEntidad(null));
        assertNull(mapper.entidadAResponse(null));
    }

}
