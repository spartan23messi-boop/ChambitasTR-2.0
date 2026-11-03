
package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CURSOS", schema = "ESCUELA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CURSO")
    private Long idCurso;

    @Column(name = "NOMBRE", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    @Column(name = "CREDITOS", nullable = false, precision = 2)
    private Integer creditos;

    // UN CURSO PUEDE TENER VARIOS GRUPOS
    @Builder.Default
    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Grupo> grupos = new ArrayList<>();

    // VALIDAR DATOS DEL CURSO
    private static void validarDatos(String nombre, String descripcion, Integer creditos) {
        StringCustomUtils.validarTamanio(nombre == null ? null : nombre.trim(), 1, 100,
                "El nombre es requerido y debe tener entre 1 y 100 caracteres");
        if (descripcion != null && descripcion.trim().length() > 200)
            throw new DatoInvalidoException("La descripción no puede superar 200 caracteres");
        if (creditos == null || creditos < 1 || creditos > 99)
            throw new DatoInvalidoException("Los créditos deben estar entre 1 y 99");
    }

    // CREAR CURSO
    public static Curso crear(String nombre, String descripcion, Integer creditos) {
        validarDatos(nombre, descripcion, creditos);
        return Curso.builder().nombre(nombre.trim())
                .descripcion(descripcion == null ? null : descripcion.trim())
                .creditos(creditos).build();
    }

    // ACTUALIZAR CURSO
    public void actualizar(String nombre, String descripcion, Integer creditos) {
        validarDatos(nombre, descripcion, creditos);
        this.nombre = nombre.trim();
        this.descripcion = descripcion == null ? null : descripcion.trim();
        this.creditos = creditos;
    }
}
