
package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import com.leonardo.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "AULAS", schema = "ESCUELA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA")
    private Long idAula;

    @Column(name = "NOMBRE", nullable = false, unique = true, length = 30)
    private String nombre;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    @Builder.Default
    @OneToMany(mappedBy = "aula", fetch = FetchType.LAZY)
    private List<Grupo> grupos = new ArrayList<>();

    private static void validarDatos(String nombre, Integer capacidad) {
        StringCustomUtils.validarTamanio(nombre == null ? null : nombre.trim(), 1, 30,
                "El nombre del aula debe tener entre 1 y 30 caracteres");
        if (capacidad == null || capacidad < 1 || capacidad > 999)
            throw new DatoInvalidoException("La capacidad debe estar entre 1 y 9999");
    }

    public static Aula crear(String nombre, Integer capacidad) {
        validarDatos(nombre, capacidad);
        return Aula.builder().nombre(nombre.trim()).capacidad(capacidad).build();
    }

    public void actualizar(String nombre, Integer capacidad) {
        validarDatos(nombre, capacidad);
        this.nombre = nombre.trim();
        this.capacidad = capacidad;
    }

    public boolean cambiaEnDatos(String nombre, Integer capacidad) {
        validarDatos(nombre, capacidad);

        return !Objects.equals(this.nombre, StringCustomUtils.limpiar(nombre))
                || !Objects.equals(this.capacidad, capacidad);
    }
}
