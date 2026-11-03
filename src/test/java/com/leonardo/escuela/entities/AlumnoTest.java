
package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlumnoTest {

    // ---------- CREAR ----------

    @Test
    void CrearAlumno_cuandoDatosSonValidos() {

        // Arrange
        String nombre = "Ana";
        String paterno = "García";
        String materno = "López";
        String email = "ana@escuela.com";
        String matricula = "A2026001";

        // Act
        Alumno alumno = Alumno.crear(
                nombre, paterno, materno, email, matricula);

        // Assert
        assertThat(alumno.getNombre()).isEqualTo(nombre);
        assertThat(alumno.getApellidoPaterno()).isEqualTo(paterno);
        assertThat(alumno.getApellidoMaterno()).isEqualTo(materno);
        assertThat(alumno.getEmail()).isEqualTo(email);
        assertThat(alumno.getMatricula()).isEqualTo(matricula);
        assertThat(alumno.getFechaIngreso()).isNotNull();
        assertThat(alumno.getInscripciones()).isEmpty();
    }

    @Test
    void crearAlumno_debeRechazarNombreVacio() {

        // Act + Assert
        assertThatThrownBy(() -> Alumno.crear(
                "   ", "García", "López",
                "ana@escuela.com", "A2026001"
        )).isInstanceOf(DatoInvalidoException.class);
    }

    // ---------- ACTUALIZAR ----------

    @Test
    void actualizar_debeModificarDatos_cuandoSonValidos() {

        // Arrange
        Alumno alumno = Alumno.crear(
                "Ana", "García", "López",
                "ana@escuela.com", "A2026001");

        // Act
        alumno.actualizar(
                "María", "Pérez", "Torres",
                "maria@escuela.com", "A2026002");

        // Assert
        assertThat(alumno.getNombre()).isEqualTo("María");
        assertThat(alumno.getApellidoPaterno()).isEqualTo("Pérez");
        assertThat(alumno.getApellidoMaterno()).isEqualTo("Torres");
        assertThat(alumno.getEmail()).isEqualTo("maria@escuela.com");
        assertThat(alumno.getMatricula()).isEqualTo("A2026002");
    }

    @Test
    void actualizar_debeConservarDatos_cuandoEmailEsInvalido() {

        // Arrange
        Alumno alumno = Alumno.crear(
                "Ana", "García", "López",
                "ana@escuela.com", "A2026001");

        // Act + Assert
        assertThatThrownBy(() -> alumno.actualizar(
                "María", "Pérez", "Torres",
                "correo-invalido", "A2026002"
        )).isInstanceOf(DatoInvalidoException.class);

        // VERIFICAR QUE NO SE MODIFICÓ
        assertThat(alumno.getNombre()).isEqualTo("Ana");
        assertThat(alumno.getEmail()).isEqualTo("ana@escuela.com");
        assertThat(alumno.getMatricula()).isEqualTo("A2026001");
    }

    // ---------- VALIDACIONES ----------

    @Test
    void validarDatos_debeAceptarEmailYMatriculaValidos() {

        // Act
        Alumno alumno = Alumno.crear(
                " Ana ", " García ", " López ",
                " ANA@ESCUELA.COM ", " A2026001 ");

        // Assert
        assertThat(alumno.getNombre()).isEqualTo("Ana");
        assertThat(alumno.getEmail()).isEqualTo("ana@escuela.com");
        assertThat(alumno.getMatricula()).isEqualTo("A2026001");
    }

    @Test
    void validarDatos_debeRechazarMatriculaMuyLarga() {

        // Act + Assert
        assertThatThrownBy(() -> Alumno.crear(
                "Ana", "García", "López",
                "ana@escuela.com", "A20260012345"
        ))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("matrícula");
    }
}
