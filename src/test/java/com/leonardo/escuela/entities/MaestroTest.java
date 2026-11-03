
package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MaestroTest {

    // ---------- CREAR ----------
    @Test
    void crear_debeCrearMaestro_cuandoDatosSonValidos() {

        // Arrange
        String nombre = "Juan";
        String apellidoPaterno = "Pérez";
        String apellidoMaterno = "López";
        String email = "juan@escuela.com";
        String telefono = "2221234567";

        // Act
        Maestro maestro = Maestro.crear(
                nombre, apellidoPaterno, apellidoMaterno,
                email, telefono
        );

        // Assert
        assertThat(maestro.getNombre()).isEqualTo(nombre);
        assertThat(maestro.getApellidoPaterno()).isEqualTo(apellidoPaterno);
        assertThat(maestro.getApellidoMaterno()).isEqualTo(apellidoMaterno);
        assertThat(maestro.getEmail()).isEqualTo(email);
        assertThat(maestro.getTelefono()).isEqualTo(telefono);
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoCampoEsVacio() {

        // Act + Assert
        assertThatThrownBy(() -> Maestro.crear(
                "Leon",
                "",
                "López",
                "juan@escuela.com",
                "2221234567"
        )).isInstanceOf(DatoInvalidoException.class);
    }
    // ---------- ACTUALIZAR ----------
    @Test
    void actualizar_debeModificarMaestro_cuandoDatosSonValidos() {
        // Arrange
        Maestro maestro = Maestro.crear(
                "Juan", "Pérez", "López",
                "juan@escuela.com", "2221234567"
        );
        // Act
        maestro.actualizar(
                "Carlos", "García", "Ramírez",
                "carlos@escuela.com", "2229876543"
        );
        // Assert
        assertThat(maestro.getNombre()).isEqualTo("Carlos");
        assertThat(maestro.getApellidoPaterno()).isEqualTo("García");
        assertThat(maestro.getApellidoMaterno()).isEqualTo("Ramírez");
        assertThat(maestro.getEmail()).isEqualTo("carlos@escuela.com");
        assertThat(maestro.getTelefono()).isEqualTo("2229876543");
    }
    @Test
    void actualizar_debeLanzarExcepcion_cuandoEmailEsInvalido() {

        // Arrange
        Maestro maestro = Maestro.crear(
                "Juan", "Pérez", "López",
                "juan@escuela.com", "2221234567"
        );

        // Act + Assert
        assertThatThrownBy(() -> maestro.actualizar(
                "Carlos", "García", "Ramírez",
                "correo-invalido", "2229876543"
        )).isInstanceOf(DatoInvalidoException.class);

        // Verificar que conserva los datos anteriores
        assertThat(maestro.getNombre()).isEqualTo("Juan");
        assertThat(maestro.getEmail()).isEqualTo("juan@escuela.com");
    }

    // ---------- VALIDAR DATOS ----------

    @Test
    void validarDatos_debeAceptarEmailYTelefonoValidos() {

        // Act: crear() ejecuta validarDatos() internamente
        Maestro maestro = Maestro.crear(
                "Ana", "Gómez", "Torres",
                "ana@escuela.com", "2225551234"
        );

        // Assert
        assertThat(maestro.getEmail()).isEqualTo("ana@escuela.com");
        assertThat(maestro.getTelefono()).isEqualTo("2225551234");
    }

    @Test
    void validarDatos_debeRechazarTelefonoNoNumerico() {

        // Act + Assert
        assertThatThrownBy(() -> Maestro.crear(
                "Ana", "Gómez", "Torres",
                "ana@escuela.com", "abcdefghij"
        ))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("teléfono");
    }
}
