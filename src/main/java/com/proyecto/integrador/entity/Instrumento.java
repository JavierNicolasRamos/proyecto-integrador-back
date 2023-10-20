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
    private String detalle;

    @ManyToOne
    private Categoria categoria;

    @OneToMany(mappedBy = "imagen")
    @JsonIgnore
    private List<Imagen> imagen; //TODO: HACER HOY

    @OneToMany(mappedBy = "instrumento")
    @JsonIgnore
    private List<Reserva> reservas;

}
