
package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "GRUPOS",
        schema = "ESCUELA",
        uniqueConstraints = @UniqueConstraint(
                name = "GRUPO_CU_MA_AU_PE_UK",
                columnNames = {
                        "ID_CURSO", "ID_MAESTRO", "ID_AULA", "PERIODO"
                }
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO")
    private Long idGrupo;

    // CADA GRUPO PERTENECE A UN CURSO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    // CADA GRUPO TIENE UN MAESTRO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_MAESTRO", nullable = false)
    private Maestro maestro;

    // CADA GRUPO UTILIZA UN AULA
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aula aula;

    @Column(name = "PERIODO", nullable = false, length = 20)
    private String periodo;

    // ALUMNOS INSCRITOS MEDIANTE SUS INSCRIPCIONES
    @Builder.Default
    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    // HORARIOS DEL GRUPO
    @Builder.Default
    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private List<Horario> horarios = new ArrayList<>();

    // VALIDAR RELACIONES Y PERÍODO DEL GRUPO
    private static void validarDatos(Curso curso, Maestro maestro, Aula aula, String periodo) {
        if (curso == null || maestro == null || aula == null)
            throw new DatoInvalidoException("Curso, maestro y aula son requeridos");
        StringCustomUtils.validarTamanio(periodo == null ? null : periodo.trim(), 1, 20,
                "El período es requerido y debe tener entre 1 y 20 caracteres");
    }

    // CREAR GRUPO
    public static Grupo crear(Curso curso, Maestro maestro, Aula aula, String periodo) {
        validarDatos(curso, maestro, aula, periodo);
        return Grupo.builder().curso(curso).maestro(maestro).aula(aula)
                .periodo(periodo.trim()).build();
    }

    // ACTUALIZAR GRUPO
    public void actualizar(Curso curso, Maestro maestro, Aula aula, String periodo) {
        validarDatos(curso, maestro, aula, periodo);
        this.curso = curso;
        this.maestro = maestro;
        this.aula = aula;
        this.periodo = periodo.trim();
    }
}
