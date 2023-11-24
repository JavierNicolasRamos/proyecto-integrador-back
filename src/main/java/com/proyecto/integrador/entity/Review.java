package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "REVIEWS")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String reviewName;

    @NotNull
    @NotEmpty
    private String reviewDescription;

    @NotNull
    @Min(value = 0, message = "El campo 'score' debe ser igual o mayor que 0")
    @Max(value = 5, message = "El campo 'score' debe ser igual o menor que 5")
    private Double score;

    private LocalDateTime reviewDateTime;

    private Boolean deleted;

    @ManyToOne
    private User boyer;

    @ManyToOne
    private Instrument instrument;

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", reviewName='" + reviewName + '\'' +
                ", reviewDescription='" + reviewDescription + '\'' +
                ", score=" + score +
                ", reviewDateTime=" + reviewDateTime +
                ", deleted=" + deleted +
                '}';
    }
}
