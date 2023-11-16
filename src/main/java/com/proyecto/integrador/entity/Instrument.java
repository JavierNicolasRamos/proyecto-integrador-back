package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@Table(name = "INSTRUMENTS")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El nombre no puede estar en blanco")
    @NotEmpty(message = "La nombre no puede estar vacío")
    @Size(min = 3, message = "El nombre debe tener al menos 3 carácteres")
    private String name;

    private LocalDate uploadDate;

    private LocalDate updateDate;

    private Boolean available;

    @NotBlank(message = "El detalle no puede estar en blanco")
    @NotEmpty(message = "La detalle no puede estar vacío")
    @Size(min = 10, message = "El detalle debe tener al menos 10 carácteres")
    @Column(columnDefinition = "TEXT")
    private String detail;

    private Boolean deleted;

    private Double score;

    @ManyToOne
    @NotNull(message = "La categoría no puede ser nula")
    private Category category;

    @OneToMany
    private List<Image> image;

    @OneToMany(mappedBy = "instrument")
    @JsonIgnore
    private List<Booking> bookings;

    @OneToMany
    private List<Characteristic> characteristics;

    @Override
    public String toString() {
        String reservasString = (bookings != null && !bookings.isEmpty()) ? bookings.iterator().next().toStringIndented(1) : "[]";
        String imagenesString = (image != null && !image.isEmpty()) ? image.iterator().next().toStringIndented(1) : "[]";

        return "Instrument{" +
                "\n  id= " + this.id +
                "\n  name= " + this.name +
                "\n  category= " + this.category.getId() +
                "\n  detail= " + this.detail +
                "\n  score= " + this.score +
                "\n  imagen= " + imagenesString +
                "\n  reservas= " + reservasString +
                "\n  uploadDate= " + this.uploadDate +
                "\n  updateDate= " + this.updateDate +
                "\n  available= " + this.available +
                "\n  deleted= " + this.deleted +
                "\n}";
    }

    public String toStringIndented(int indentLevel) {
        String reservasString = (bookings != null && !bookings.isEmpty()) ? bookings.iterator().next().toStringIndented(1) : "[]";
        String imagenesString = (image != null && !image.isEmpty()) ? image.iterator().next().toStringIndented(1) : "[]";
        StringBuilder indent = new StringBuilder();
        indent.append("  ".repeat(Math.max(0, indentLevel)));

        return "Instrument{" +
                "\n" + indent + "  id= " + this.id +
                "\n" + indent + "  name= " + this.name +
                "\n" + indent + "  category= " + this.category.getId() +
                "\n" + indent + "  detail= " + this.detail +
                "\n" + indent + "  score= " + this.score +
                "\n" + indent + "  imagen= " + imagenesString +
                "\n" + indent + "  reservas= " + reservasString +
                "\n" + indent + "  uploadDate= " + this.uploadDate +
                "\n" + indent + "  updateDate= " + this.updateDate +
                "\n" + indent + "  available= " + this.available +
                "\n" + indent + "  deleted= " + this.deleted +
                "\n" + indent + "}";
    }
}
