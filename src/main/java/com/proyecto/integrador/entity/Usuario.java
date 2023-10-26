package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "USUARIOS")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Reserva> reservas;

    private String nombre;
    private String apellido;
    private Boolean admin;
    private int codigoArea;
    private int prefijo;
    private int numero;
    private Boolean movil;


    public String toStringIndented(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        String reservasString = (reservas != null && !reservas.isEmpty()) ? reservas.iterator().next().toStringIndented(1) : "[]";

        return "Contacto{" +
                "\n" + indent + "  id= " + this.id +
                "\n" + indent + "  reservas= " + reservasString +
                "\n" + indent + "  nombre= " + this.nombre +
                "\n" + indent + "  apellido= " + this.apellido +
                "\n" + indent + "  admin= " + this.admin +
                "\n" + indent + "  codigoArea= " + this.codigoArea +
                "\n" + indent + "  prefijo= " + this.prefijo +
                "\n" + indent + "  numero= " + this.numero +
                "\n" + indent + "  movil= " + this.movil +
                "\n" + indent + "}";
    }
}
