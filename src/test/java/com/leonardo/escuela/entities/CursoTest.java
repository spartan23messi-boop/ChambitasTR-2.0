package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CursoTest {
@Test void crear_debeAceptarDatosValidos() {
        Curso curso = Curso.crear(" Matemáticas ", "Álgebra", 5);
        assertThat(curso.getNombre()).isEqualTo("Matemáticas");
        assertThat(curso.getCreditos()).isEqualTo(5);
        assertThat(curso.getGrupos()).isEmpty();
    }
    @Test void crear_debeRechazarNombreVacio() {
        assertThatThrownBy(() -> Curso.crear("  ", "Álgebra", 5))
                .isInstanceOf(DatoInvalidoException.class);
    }
    @Test void actualizar_debeCambiarDatos() {
        Curso curso = Curso.crear("Matemáticas", "Álgebra", 5);
        curso.actualizar("Física", "Mecánica", 6);
        assertThat(curso.getNombre()).isEqualTo("Física");
        assertThat(curso.getDescripcion()).isEqualTo("Mecánica");
        assertThat(curso.getCreditos()).isEqualTo(6);
    }
    @Test void actualizar_debeConservarDatosSiCreditosInvalidos() {
        Curso curso = Curso.crear("Matemáticas", "Álgebra", 5);
        assertThatThrownBy(() -> curso.actualizar("Física", "Mecánica", -1))
                .isInstanceOf(DatoInvalidoException.class);
        assertThat(curso.getNombre()).isEqualTo("Matemáticas");
        assertThat(curso.getCreditos()).isEqualTo(5);
    }
    @Test void validarDatos_debeAceptarDescripcionOpcional() {
        assertThat(Curso.crear("Matemáticas", null, 5).getDescripcion()).isNull();
    }
    @Test void validarDatos_debeRechazarDescripcionLarga() {
        assertThatThrownBy(() -> Curso.crear("Matemáticas", "a".repeat(201), 5))
                .isInstanceOf(DatoInvalidoException.class);
    }
}
