package com.proyecto.integrador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.integrador.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "El campo 'nombre' no puede ser nulo")
    @NotBlank(message = "El campo 'nombre' no puede estar en blanco")
    private String name;

    @NotNull(message = "El campo 'apellido' no puede ser nulo")
    @NotBlank(message = "El campo 'apellido' no puede estar en blanco")
    private String surname;

    @NotNull(message = "El campo 'codigoArea' no puede ser nulo")
    private Integer areaCode;

    @NotNull(message = "El campo 'prefijo' no puede ser nulo")
    private Integer prefix;

    @NotNull(message = "El campo 'numero' no puede ser nulo")
    private Integer phone;

    @NotNull(message = "El campo 'movil' no puede ser nulo")
    private Boolean isMobile;

    @NotNull(message = "El campo 'email' no puede ser nulo")
    @NotBlank(message = "El campo 'email' no puede estar en blanco")
    @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida")
    @Column(unique = true)
    private String email;

    @NotNull(message = "El campo 'password' no puede ser nulo")
    @NotBlank(message = "El campo 'password' no puede estar en blanco")
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Booking> bookings;

    @Column(length = 10, columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    private Role userRole;

    private Boolean isActive;

    private Boolean deleted;

    @ManyToMany
    private List<Instrument> favourites;

    public String toStringIndented(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        indent.append("  ".repeat(Math.max(0, indentLevel)));

        return "User{" +
                "\n" + indent + "  id= " + this.id +
                "\n" + indent + "  role= " + this.userRole +
                "\n" + indent + "  bookings= " + this.bookings +
                "\n" + indent + "  name= " + this.name +
                "\n" + indent + "  surname= " + this.surname +
                "\n" + indent + "  email= " + this.email +
                "\n" + indent + "  areaCode= " + this.areaCode +
                "\n" + indent + "  prefix= " + this.prefix +
                "\n" + indent + "  phone= " + this.phone +
                "\n" + indent + "  isMobile= " + this.isMobile +
                "\n" + indent + "  isActive= " + this.isActive +
                "\n" + indent + "  deleted= " + this.deleted +
                "\n" + indent + "}";
    }
}