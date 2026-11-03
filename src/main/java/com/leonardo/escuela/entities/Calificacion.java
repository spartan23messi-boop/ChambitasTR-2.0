
package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CALIFICACIONES", schema = "ESCUELA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CALIFICACION")
    private Long idCalificacion;

    // CADA CALIFICACIÓN CORRESPONDE A UNA INSCRIPCIÓN
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "ID_INSCRIPCION",
            nullable = false,
            unique = true
    )
    private Inscripcion inscripcion;

    // CALIFICACIÓN CON UN DECIMAL
    @Column(
            name = "CALIFICACION",
            nullable = false,
            precision = 3,
            scale = 1
    )
    private BigDecimal calificacion;

    @Column(name = "FECHA_REGISTRO")
    private LocalDate fechaRegistro;

    // VALIDAR CALIFICACIÓN SEGÚN LA PRECISIÓN NUMBER(3,1)
    private static void validarDatos(Inscripcion inscripcion, BigDecimal valor) {
        if (inscripcion == null)
            throw new DatoInvalidoException("La inscripción es requerida");
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0
                || valor.compareTo(new BigDecimal("99.9")) > 0
                || valor.stripTrailingZeros().scale() > 1)
            throw new DatoInvalidoException("La calificación debe estar entre 0 y 99.9 y tener máximo un decimal");
    }

    // CREAR CALIFICACIÓN
    public static Calificacion crear(Inscripcion inscripcion, BigDecimal valor) {
        validarDatos(inscripcion, valor);
        return Calificacion.builder().inscripcion(inscripcion)
                .calificacion(valor).fechaRegistro(LocalDate.now()).build();
    }

    // ACTUALIZAR CALIFICACIÓN SIN CAMBIAR LA INSCRIPCIÓN
    public void actualizar(BigDecimal valor) {
        validarDatos(this.inscripcion, valor);
        this.calificacion = valor;
        this.fechaRegistro = LocalDate.now();
    }
}
