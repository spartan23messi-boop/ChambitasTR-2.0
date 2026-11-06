package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AulaTest {
@Test void crear_debeAceptarDatosValidos() {
        Aula aula = Aula.crear(" A101 ", 30);
        assertThat(aula.getNombre()).isEqualTo("A101");
        assertThat(aula.getCapacidad()).isEqualTo(30);
        assertThat(aula.getGrupos()).isEmpty();
    }
    @Test void crear_debeRechazarNombreVacio() {
        assertThatThrownBy(() -> Aula.crear("  ", 30))
                .isInstanceOf(DatoInvalidoException.class);
    }
    @Test void actualizar_debeCambiarDatos() {
        Aula aula = Aula.crear("A101", 30);
        aula.actualizar("B201", 40);
        assertThat(aula.getNombre()).isEqualTo("B201");
        assertThat(aula.getCapacidad()).isEqualTo(40);
    }
    @Test void actualizar_debeConservarDatosAnteCapacidadInvalida() {
        Aula aula = Aula.crear("A101", 30);
        assertThatThrownBy(() -> aula.actualizar("B201", -3))
                .isInstanceOf(DatoInvalidoException.class);
        assertThat(aula.getNombre()).isEqualTo("A101");
        assertThat(aula.getCapacidad()).isEqualTo(30);
    }
    @Test void validarDatos_debeAceptarCapacidadPositiva() {
        assertThat(Aula.crear("A101", 1).getCapacidad()).isEqualTo(1);
    }
    @Test void validarDatos_debeRechazarCapacidadNula() {
        assertThatThrownBy(() -> Aula.crear("A101", null))
                .isInstanceOf(DatoInvalidoException.class);
    }
}
