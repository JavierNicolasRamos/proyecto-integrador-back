package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "INSTRUMENTOS")
@Getter
@Setter
@NoArgsConstructor
public class Instrumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El nombre no puede estar en blanco")
    @NotEmpty(message = "La nombre no puede estar vacío")
    @Size(min = 3, message = "El nombre debe tener al menos 3 carácteres")
    private String nombre;

    private LocalDate fechaCarga;
    private LocalDate fechaUpdate;
    private Boolean disponible;

    @NotBlank(message = "El detalle no puede estar en blanco")
    @NotEmpty(message = "La detalle no puede estar vacío")
    @Size(min = 10, message = "El detalle debe tener al menos 10 carácteres")
    @Column(columnDefinition = "TEXT")
    private String detalle;

    private Boolean eliminado;
    private Double puntuacion;

    @ManyToOne
    @NotNull(message = "La categoría no puede ser nula")
    private Categoria categoria;

    @OneToMany(mappedBy = "instrumento")
    private List<Imagen> imagen;

    @OneToMany(mappedBy = "instrumento")
    @JsonIgnore
    private List<Reserva> reservas;

    @Override
    public String toString() {
        String reservasString = (reservas != null && !reservas.isEmpty()) ? reservas.iterator().next().toStringIndented(1) : "[]";
        String imagenesString = (imagen != null && !imagen.isEmpty()) ? imagen.iterator().next().toStringIndented(1) : "[]";

        return "Instrumento{" +
                "\n  id= " + this.id +
                "\n  nombre= " + this.nombre +
                "\n  fechaCarga= " + this.fechaCarga +
                "\n  fechaUpdate= " + this.fechaUpdate +
                "\n  disponible= " + this.disponible +
                "\n  detalle= " + this.detalle +
                "\n  eliminado= " + this.eliminado +
                "\n  puntuacion= " + this.puntuacion +
                "\n  categoria= " + this.categoria.getId() +
                "\n  imagen= " + imagenesString +
                "\n  reservas= " + reservasString +
                "\n}";
    }

}
