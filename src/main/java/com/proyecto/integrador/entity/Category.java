package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @NotBlank(message = "El nombre no puede estar en blanco")
    @NotEmpty(message = "El nombre no puede estar vacía")
    private String name;

    @NotBlank(message = "La descripcón no puede estar en blanco")
    @NotEmpty(message = "La descripción no puede estar vacía")
    private String details;

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

    public String toStringIndented(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        indent.append("  ".repeat(Math.max(0, indentLevel)));

        return "Category{" +
                "\n" + indent + "  id= " + this.id +
                "\n" + indent + "  name= " + this.name +
                "\n" + indent + "  instrument= " + this.instrument +
                "\n" + indent + "  deleted= " + this.deleted +
                "\n" + indent + "}";
    }
}
