package com.proyecto.integrador.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "CHARACTERISTICS")
public class Characteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String icon;
    private Boolean deleted;

    @Override
    public String toString() {
        return "Characteristic{" +
                "\n  id=" + id +
                "\n  name='" + name + '\'' +
                "\n  icon='" + icon + '\'' +
                "\n}";
    }
}
