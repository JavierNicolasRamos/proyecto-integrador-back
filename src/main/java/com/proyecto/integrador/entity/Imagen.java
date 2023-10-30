package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "La imagen no puede estar en blanco")
    @NotEmpty(message = "La imagen no puede estar vacía")
    private String imagen;

    @ManyToOne
    @NotNull(message = "El instrumento no puede ser nulo")
    @JsonIgnore
    private Instrumento instrumento;

    private Boolean eliminado;



    @Override
    public String toString() {
        return "Imagen{" +
                "\n  id= " + this.id +
                "\n  imagen= " + this.imagen +
                "\n  eliminado= " + this.eliminado +
                "\n}";
    }


    public String toStringIndented(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        return "Contacto{" +
                "\n" + indent + "  id= " + this.id +
                "\n" + indent + "  imagen= " + this.imagen +
                "\n" + indent + "  nombre= " + this.eliminado +
                "\n" + indent + "}";
    }
}
