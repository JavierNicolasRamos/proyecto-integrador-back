package com.proyecto.integrador.entity;

import jakarta.persistence.*;
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
    @NotEmpty
    private Integer score;

    private LocalDateTime reviewDateTime;

    private Boolean deleted;

    @OneToOne(mappedBy = "review")
    private Booking booking;

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", reviewName='" + reviewName + '\'' +
                ", reviewDescription='" + reviewDescription + '\'' +
                ", score=" + score +
                ", reviewDateTime=" + reviewDateTime +
                ", deleted=" + deleted +
                ", booking=" + booking +
                '}';
    }
}
