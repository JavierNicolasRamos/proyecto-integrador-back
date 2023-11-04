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

    @OneToOne(mappedBy = "categoria")
    private Imagen imagen;

    private Boolean eliminado;

    @Override
    public String toString() {
        return "Categoria{" +
                "\n  id= " + this.id +
                "\n  descripcion= " + this.descripcion +
                "\n  instrumento= " + this.instrumento +
                "\n  eliminado= " + this.eliminado +
                "\n}";
    }



}
