package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "CATEGORIAS")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "La descripcion no puede estar en blanco")
    @NotEmpty(message = "La descripcion no puede estar vacía")
    @Size(min = 5, message = "La descripcion debe tener al menos 5 carácteres")
    private String descripcion;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Instrumento> instrumento;
    //No hace falta indicar el nombre de la columna, ya que posee el mismo nombre. Eliminar la linea.
    private Boolean eliminado;
}
