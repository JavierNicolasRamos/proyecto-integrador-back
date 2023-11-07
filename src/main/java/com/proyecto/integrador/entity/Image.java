package com.proyecto.integrador.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "IMAGES")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "La imagen no puede estar en blanco")
    @NotEmpty(message = "La imagen no puede estar vacía")
    private String image;

    private Boolean deleted;

    @Override
    public String toString() {
        return "Image{" +
                "\n  id= " + this.id +
                "\n  image= " + this.image +
                "\n  deleted= " + this.deleted +
                "\n}";
    }

    public String toStringIndented(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        indent.append("  ".repeat(Math.max(0, indentLevel)));

        return "Image{" +
                "\n" + indent + "  id= " + this.id +
                "\n" + indent + "  image= " + this.image +
                "\n" + indent + "  deleted= " + this.deleted +
                "\n" + indent + "}";
    }
}
