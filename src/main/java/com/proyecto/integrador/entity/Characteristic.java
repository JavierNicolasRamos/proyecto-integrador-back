package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ManyToMany(mappedBy = "characteristics")
    @JsonIgnore
    private List<Instrument> instruments;

    @Override
    public String toString() {
        return "Characteristic{" +
                "\n  id=" + id +
                "\n  name='" + name + '\'' +
                "\n  icon='" + icon + '\'' +
                "\n}";
    }

    public String toStringIndented(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        indent.append("  ".repeat(Math.max(0, indentLevel)));

        return "Characteristic{" +
                "\n" + indent + "  id=" + id +
                "\n" + indent + "  name='" + name + '\'' +
                "\n" + indent + "  icon='" + icon + '\'' +
                "\n" + indent + "}";
    }
}
