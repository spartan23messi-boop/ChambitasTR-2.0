
package com.leonardo.escuela.entities;

import com.leonardo.escuela.enums.DiaSemana;
import com.leonardo.escuela.exceptions.DatoInvalidoException;
import java.text.Normalizer;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HORARIOS", schema = "ESCUELA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long idHorario;

    // GRUPO AL QUE PERTENECE EL HORARIO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Column(name = "DIA", nullable = false, length = 15)
    private String dia;

    // ORACLE GUARDA LAS HORAS COMO VARCHAR2(5)
    @Column(name = "HORA_INICIO", nullable = false, length = 5)
    private String horaInicio;

    @Column(name = "HORA_FIN", nullable = false, length = 5)
    private String horaFin;

    // VALIDAR DÍA Y HORAS DEL GRUPO
    private static void validarDatos(Grupo grupo, String dia, String inicio, String fin) {
        if (grupo == null)
            throw new DatoInvalidoException("El grupo es requerido");
        normalizarDia(dia);
        LocalTime horaInicio = convertirHora(inicio);
        LocalTime horaFin = convertirHora(fin);
        if (!horaInicio.isBefore(horaFin))
            throw new DatoInvalidoException("La hora de inicio debe ser anterior a la hora final");
    }

    private static String normalizarDia(String dia) {
        if (dia == null || dia.isBlank())
            throw new DatoInvalidoException("El día es requerido");
        String normalizado = Normalizer.normalize(dia.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toUpperCase(Locale.ROOT);
        try {
            DiaSemana.valueOf(normalizado);
            return normalizado;
        } catch (IllegalArgumentException ex) {
            throw new DatoInvalidoException("El día de la semana no es válido");
        }
    }

    private static LocalTime convertirHora(String hora) {
        if (hora == null || !hora.matches("\\d{2}:\\d{2}"))
            throw new DatoInvalidoException("La hora debe tener formato HH:mm");
        try {
            return LocalTime.parse(hora);
        } catch (DateTimeParseException ex) {
            throw new DatoInvalidoException("La hora no es válida");
        }
    }

    // CREAR HORARIO
    public static Horario crear(Grupo grupo, String dia, String inicio, String fin) {
        validarDatos(grupo, dia, inicio, fin);
        return Horario.builder().grupo(grupo).dia(normalizarDia(dia))
                .horaInicio(inicio).horaFin(fin).build();
    }

    // ACTUALIZAR HORARIO
    public void actualizar(Grupo grupo, String dia, String inicio, String fin) {
        validarDatos(grupo, dia, inicio, fin);
        this.grupo = grupo;
        this.dia = normalizarDia(dia);
        this.horaInicio = inicio;
        this.horaFin = fin;
    }
}
