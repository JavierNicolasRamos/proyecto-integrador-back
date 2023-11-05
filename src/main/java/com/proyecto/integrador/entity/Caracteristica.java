package com.proyecto.integrador.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CARACTERISTICAS")
@Getter
@Setter
@NoArgsConstructor
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String nombre;
    private String icono;
    private Boolean eliminada;

    @Override
    public String toString() {
        return "Caracteristica{" +
                "\n  id=" + id +
                "\n  nombre='" + nombre + '\'' +
                "\n  icono='" + icono + '\'' +
                "\n}";
    }

}
