
package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "ALUMNOS", schema = "ESCUELA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALUMNO")
    private Long idAlumno;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "MATRICULA", nullable = false, unique = true, length = 10)
    private String matricula;

    @Builder.Default
    @Column(name = "FECHA_INGRESO")
    private LocalDate fechaIngreso = LocalDate.now();

    // UN ALUMNO PUEDE TENER VARIAS INSCRIPCIONES
    @Builder.Default
    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    // VALIDAR DATOS ANTES DE CREAR O ACTUALIZAR
    private static void validarDatos(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String matricula) {

        StringCustomUtils.validarTamanio(
                limpiar(nombre), 1, 50,
                "El nombre es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(
                limpiar(apellidoPaterno), 1, 50,
                "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(
                limpiar(apellidoMaterno), 1, 50,
                "El apellido materno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(
                limpiar(email), 8, 100,
                "El email es requerido y debe tener entre 8 y 100 caracteres");

        if (!email.trim().matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$"))
            throw new DatoInvalidoException("El email no tiene un formato válido");


        StringCustomUtils.validarTamanio(
                limpiar(matricula), 1, 10,
                "La matrícula es requerida y debe tener entre 1 y 10 caracteres");
    }

    // ELIMINAR ESPACIOS SIN PROVOCAR NULLPOINTEREXCEPTION
    private static String limpiar(String texto) {
        return texto == null ? null : texto.trim();
    }

    // CREAR ALUMNO
    public static Alumno crear(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String matricula) {

        validarDatos(nombre, apellidoPaterno,
                apellidoMaterno, email, matricula);

        return Alumno.builder()
                .nombre(nombre.trim())
                .apellidoPaterno(apellidoPaterno.trim())
                .apellidoMaterno(apellidoMaterno.trim())
                .email(email.trim().toLowerCase(Locale.ROOT))
                .matricula(matricula.trim())
                .build();
    }

    // ACTUALIZAR ALUMNO
    public void actualizar(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String matricula) {

        validarDatos(nombre, apellidoPaterno,
                apellidoMaterno, email, matricula);

        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.email = email.trim().toLowerCase(Locale.ROOT);
        this.matricula = matricula.trim();
    }
}
