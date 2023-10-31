package com.proyecto.integrador.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CARACTERISTICAS")
@Getter
@Setter
@NoArgsConstructor
public class Caracteristicas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String nombre;
    private String icono;
    @ManyToOne
    @JoinColumn(name = "instrumento_id")
    private Instrumento instrumento;

    @Override
    public String toString() {
        return "Caracteristicas{" +
                "\n  id=" + id +
                "\n  nombre='" + nombre + '\'' +
                "\n  icono='" + icono + '\'' +
                "\n  instrumento=" + (instrumento != null ? instrumento.getId() : "null") +
                "\n}";
    }

}
