package com.proyecto.integrador.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "RESERVAS")
@Getter
@Setter
@NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Usuario proveedor;

    @ManyToOne
    private Instrumento instrumento;

    private Boolean reservaActiva;
    private LocalDate inicioReserva;
    private LocalDate finReserva;
    private Boolean eliminado;
    private String email;

    @Override
    public String toString(){
        return "Cliente{" +
                "\n  id= " + this.id +
                "\n  usuario= " + this.usuario.toStringIndented(1) +
                "\n  proveedor= " + this.proveedor.toStringIndented(1) +
                "\n  instrumento= " + this.instrumento +
                "\n  reservaActiva= " + this.reservaActiva +
                "\n  inicioReserva= " + this.inicioReserva +
                "\n  finReserva= " + this.finReserva +
                "\n  eliminado= " + this.eliminado +
                "\n  email= " + this.email +
                "\n}";
    }

    public String toStringIndented(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        return "Contacto{" +
                "\n" + indent + "  id= " + this.id +
                "\n" + indent + "  usuario= " + this.usuario.toStringIndented(1) +
                "\n" + indent + "  proveedor= " + this.proveedor.toStringIndented(1) +
                "\n" + indent + "  instrumento= " + this.instrumento +
                "\n" + indent + "  reservaActiva= " + this.reservaActiva +
                "\n" + indent + "  inicioReserva= " + this.inicioReserva +
                "\n" + indent + "  finReserva= " + this.finReserva +
                "\n" + indent + "  eliminado= " + this.eliminado +
                "\n" + indent + "  email= " + this.email +
                "\n" + indent + "}";
    }
}
