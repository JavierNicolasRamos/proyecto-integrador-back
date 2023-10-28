package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "El campo 'nombre' no puede ser nulo")
    @NotBlank(message = "El campo 'nombre' no puede estar en blanco")
    private String nombre;

    @NotNull(message = "El campo 'apellido' no puede ser nulo")
    @NotBlank(message = "El campo 'apellido' no puede estar en blanco")
    private String apellido;

    @NotNull(message = "El campo 'administrador' no puede ser nulo")
    private Boolean administrador;

    @NotNull(message = "El campo 'codigoArea' no puede ser nulo")
    @NotBlank(message = "El campo 'codigoArea' no puede estar en blanco")
    private int codigoArea;

    @NotNull(message = "El campo 'prefijo' no puede ser nulo")
    @NotBlank(message = "El campo 'prefijo' no puede estar en blanco")
    private int prefijo;

    @NotNull(message = "El campo 'numero' no puede ser nulo")
    @NotBlank(message = "El campo 'numero' no puede estar en blanco")
    private int numero;

    @NotNull(message = "El campo 'movil' no puede ser nulo")
    private Boolean movil;


    @NotNull(message = "El campo 'email' no puede ser nulo")
    @NotBlank(message = "El campo 'email' no puede estar en blanco")
    @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida")
    private String email;

    public String toStringIndented(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        String reservasString = (reservas != null && !reservas.isEmpty()) ? reservas.iterator().next().toStringIndented(1) : "[]";

        return "Contacto{" +
                "\n" + indent + "  id= " + this.id +
                "\n" + indent + "  reservas= " + this.reservas +
                "\n" + indent + "  nombre= " + this.nombre +
                "\n" + indent + "  apellido= " + this.apellido +
                "\n  email= " + this.email +
                // "\n" + indent + "  admin= " + this.admin +
                "\n" + indent + "  codigoArea= " + this.codigoArea +
                "\n" + indent + "  prefijo= " + this.prefijo +
                "\n" + indent + "  numero= " + this.numero +
                "\n" + indent + "  movil= " + this.movil +
                "\n" + indent + "}";
    }





}
