
package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Builder.Default
    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Grupo> grupos = new ArrayList<>();


    private static void validarDatos(String nombre, String descripcion, Integer creditos) {
        StringCustomUtils.validarTamanio(
                StringCustomUtils.limpiar(nombre), 1, 100,
                "El nombre es requerido y debe tener entre 1 y 100 caracteres"
        );
        if (descripcion != null && StringCustomUtils.limpiar(descripcion).length() > 200)
            throw new DatoInvalidoException("La descripción no puede superar 200 caracteres");

        if (creditos == null || creditos < 1 || creditos > 99)
            throw new DatoInvalidoException("Los créditos deben estar entre 1 y 99");
    }

    public boolean cambiaEnDatos(String nombre, String descripcion, Integer creditos) {
        validarDatos(nombre, descripcion, creditos);

        return !Objects.equals(this.nombre, StringCustomUtils.limpiar(nombre))
                || !Objects.equals(this.descripcion, StringCustomUtils.limpiar(descripcion))
                || !Objects.equals(this.creditos, creditos);
    }

    public static Curso crear(String nombre, String descripcion, Integer creditos) {
        validarDatos(nombre, descripcion, creditos);
        return Curso.builder()
                .nombre(StringCustomUtils.limpiar(nombre))
                .descripcion(StringCustomUtils.limpiar(descripcion))
                .creditos(creditos)
                .build();
    }

    public void actualizar(String nombre, String descripcion, Integer creditos) {
        validarDatos(nombre, descripcion, creditos);

        this.nombre = StringCustomUtils.limpiar(nombre);
        this.descripcion = StringCustomUtils.limpiar(descripcion);
        this.creditos = creditos;
    }
}
