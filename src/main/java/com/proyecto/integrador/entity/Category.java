package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "CATEGORIES")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "La descripcion no puede estar en blanco")
    @NotEmpty(message = "La descripcion no puede estar vacía")
    @Size(min = 5, message = "La descripcion debe tener al menos 5 carácteres")
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Instrument> instrument;

    @OneToOne
    private Image image;

    private Boolean deleted;

    @Override
    public String toString() {
        return "Category{" +
                "\n  id= " + this.id +
                "\n  name= " + this.name +
                "\n  instrument= " + this.instrument +
                "\n  deleted= " + this.deleted +
                "\n}";
    }
}
