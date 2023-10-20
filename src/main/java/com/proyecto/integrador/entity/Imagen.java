package com.proyecto.integrador.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "IMAGENES")
@Getter
@Setter
@NoArgsConstructor
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String imagen;

    @ManyToOne
    private Instrumento instrumento;

}
