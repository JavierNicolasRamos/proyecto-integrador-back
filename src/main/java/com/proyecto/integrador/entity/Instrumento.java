package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    private String nombre;
    private LocalDate fechaCarga;
    private LocalDate fechaUpdate;
    private Boolean disponible;
    @Column(columnDefinition = "TEXT")
    private String detalle;
    private Boolean eliminado;
    private Double puntuacion;

    @ManyToOne
    private Categoria categoria;

    @OneToMany(mappedBy = "instrumento")
    @JsonIgnore
    private List<Imagen> imagen;

    @OneToMany(mappedBy = "instrumento")
    @JsonIgnore
    private List<Reserva> reservas;

}
