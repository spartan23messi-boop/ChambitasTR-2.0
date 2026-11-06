package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_MAESTRO", nullable = false)
    private Maestro maestro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aula aula;

    @Column(name = "PERIODO", nullable = false, length = 20)
    private String periodo;

    @Builder.Default
    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private List<Horario> horarios = new ArrayList<>();

    private static void validarDatos(
            Curso curso,
            Maestro maestro,
            Aula aula,
            String periodo) {

        if (curso == null || maestro == null || aula == null)
            throw new DatoInvalidoException(
                    "Curso, maestro y aula son requeridos"
            );

        StringCustomUtils.validarTamanio(StringCustomUtils.limpiar(periodo), 7, 7,
                "El período debe tener formato yyyy-MM");
        if (!Pattern.matches("\\d{4}-(0[1-9]|1[0-2])", periodo.trim()))
            throw new DatoInvalidoException("El período debe tener formato yyyy-MM");
    }

    public static Grupo crear(
            Curso curso,
            Maestro maestro,
            Aula aula,
            String periodo) {

        validarDatos(curso, maestro, aula, periodo);

        return Grupo.builder()
                .curso(curso)
                .maestro(maestro)
                .aula(aula)
                .periodo(StringCustomUtils.limpiar(periodo))
                .build();
    }

    public void actualizar(
            Curso curso,
            Maestro maestro,
            Aula aula,
            String periodo) {

        validarDatos(curso, maestro, aula, periodo);

        this.curso = curso;
        this.maestro = maestro;
        this.aula = aula;
        this.periodo = StringCustomUtils.limpiar(periodo);
    }

    public boolean cambiaEnDatos(
            Curso curso,
            Maestro maestro,
            Aula aula,
            String periodo) {

        validarDatos(curso, maestro, aula, periodo);

        return !Objects.equals(
                this.curso == null ? null : this.curso.getIdCurso(),
                curso.getIdCurso())
                || !Objects.equals(
                this.maestro == null ? null : this.maestro.getIdMaestro(),
                maestro.getIdMaestro())
                || !Objects.equals(
                this.aula == null ? null : this.aula.getIdAula(),
                aula.getIdAula())
                || !Objects.equals(
                this.periodo,
                StringCustomUtils.limpiar(periodo));
    }
}