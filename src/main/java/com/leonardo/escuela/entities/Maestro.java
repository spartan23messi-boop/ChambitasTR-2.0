
package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "MAESTROS", schema = "ESCUELA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Maestro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MAESTRO")
    private Long idMaestro;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "TELEFONO", nullable = false, unique = true, length = 10)
    private String telefono;

    // RELACIÓN: UN MAESTRO PUEDE TENER VARIOS GRUPOS
    @Builder.Default
    @OneToMany(mappedBy = "maestro", fetch = FetchType.LAZY)
    private List<Grupo> grupos = new ArrayList<>();

    // CONSTRUIR NOMBRE COMPLETO PARA LAS RESPUESTAS
    public String obtenerNombreCompleto() {
        return String.join(" ", nombre, apellidoPaterno, apellidoMaterno);
    }

    // VALIDAR DATOS DEL MAESTRO
    private static void validarDatos(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String telefono) {

        // VALIDAR NOMBRE Y APELLIDOS
        StringCustomUtils.validarTamanio(nombre == null ? null : nombre.trim(), 1, 50,
                "El nombre es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoPaterno == null ? null : apellidoPaterno.trim(), 1, 50,
                "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoMaterno == null ? null : apellidoMaterno.trim(), 1, 50,
                "El apellido materno es requerido y debe tener entre 1 y 50 caracteres");

        // VALIDAR EMAIL
        StringCustomUtils.validarTamanio(email == null ? null : email.trim(), 8, 100,
                "El email es requerido y debe tener entre 8 y 100 caracteres");

        if (!email.trim().matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$"))
            throw new DatoInvalidoException(
                    "El email no tiene un formato válido");


        // VALIDAR TELÉFONO
        StringCustomUtils.validarTamanio(telefono == null ? null : telefono.trim(), 10, 10,
                "El teléfono debe tener exactamente 10 dígitos");

        if (!telefono.trim().matches("\\d{10}")) {
            throw new DatoInvalidoException(
                    "El teléfono debe contener 10 dígitos numéricos");
        }
    }

    // CREAR MAESTRO
    public static Maestro crear(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String telefono) {

        // VALIDAR ANTES DE CREAR
        validarDatos(nombre, apellidoPaterno,
                apellidoMaterno, email, telefono);

        // CONSTRUIR MAESTRO
        return Maestro.builder()
                .nombre(nombre.trim())
                .apellidoPaterno(apellidoPaterno.trim())
                .apellidoMaterno(apellidoMaterno.trim())
                .email(email.trim().toLowerCase(Locale.ROOT))
                .telefono(telefono.trim())
                .build();
    }

    // ACTUALIZAR MAESTRO
    public void actualizar(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String telefono) {

        // VALIDAR ANTES DE MODIFICAR
        validarDatos(nombre, apellidoPaterno,
                apellidoMaterno, email, telefono);

        // ACTUALIZAR ATRIBUTOS
        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.email = email.trim().toLowerCase(Locale.ROOT);
        this.telefono = telefono.trim();
    }
}
