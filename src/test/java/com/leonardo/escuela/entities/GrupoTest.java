package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GrupoTest {
private final Curso curso = new Curso();
    private final Maestro maestro = new Maestro();
    private final Aula aula = new Aula();
    @Test void crear_debeAsignarRelacionesValidas() {
        Grupo grupo = Grupo.crear(curso, maestro, aula, " 2026-A ");
        assertThat(grupo.getCurso()).isSameAs(curso);
        assertThat(grupo.getMaestro()).isSameAs(maestro);
        assertThat(grupo.getAula()).isSameAs(aula);
        assertThat(grupo.getPeriodo()).isEqualTo("2026-A");
    }
    @Test void crear_debeRechazarMaestroNulo() {
        assertThatThrownBy(() -> Grupo.crear(curso, null, aula, "2026-A"))
                .isInstanceOf(DatoInvalidoException.class);
    }
    @Test void actualizar_debeCambiarPeriodoYAula() {
        Grupo grupo = Grupo.crear(curso, maestro, aula, "2026-A");
        Aula otraAula = new Aula();
        grupo.actualizar(curso, maestro, otraAula, "2026-B");
        assertThat(grupo.getPeriodo()).isEqualTo("2026-B");
        assertThat(grupo.getAula()).isSameAs(otraAula);
    }
    @Test void actualizar_debeConservarDatosSiCursoNulo() {
        Grupo grupo = Grupo.crear(curso, maestro, aula, "2026-A");
        assertThatThrownBy(() -> grupo.actualizar(null, maestro, aula, "2026-B"))
                .isInstanceOf(DatoInvalidoException.class);
        assertThat(grupo.getCurso()).isSameAs(curso);
        assertThat(grupo.getPeriodo()).isEqualTo("2026-A");
    }
    @Test void validarDatos_debeAceptarPeriodoCorto() {
        assertThat(Grupo.crear(curso, maestro, aula, "A").getPeriodo()).isEqualTo("A");
    }
    @Test void validarDatos_debeRechazarPeriodoVacio() {
        assertThatThrownBy(() -> Grupo.crear(curso, maestro, aula, "   "))
                .isInstanceOf(DatoInvalidoException.class);
    }
}
