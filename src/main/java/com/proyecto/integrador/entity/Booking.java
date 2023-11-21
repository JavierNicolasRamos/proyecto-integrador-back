package com.proyecto.integrador.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "BOOKINGS")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "El campo 'usuario' no puede ser nulo")
    @ManyToOne
    private User user;

    @NotNull(message = "El campo 'instrumento' no puede ser nulo")
    @ManyToOne
    private Instrument instrument;

    private Boolean activeBooking;

    private LocalDate bookingStart;

    private LocalDate bookingEnd;

    private Boolean deleted;

//    @OneToOne
//    private Review review;


    @Override
    public String toString(){
        return "Booking{" +
                "\n  id= " + this.id +
                "\n  user= " + this.user.toStringIndented(1) +
                "\n  instrument= " + this.instrument +
                "\n  activeBooking= " + this.activeBooking +
                "\n  bookingStart= " + this.bookingStart +
                "\n  bookingEnd= " + this.bookingEnd +
                "\n  deleted= " + this.deleted +
                "\n}";
    }

    public String toStringIndented(int indentLevel) {
        StringBuilder indent = new StringBuilder();
        indent.append("  ".repeat(Math.max(0, indentLevel)));

        return "Booking{" +
                "\n" + indent + "  id= " + this.id +
                "\n" + indent + "  user= " + this.user.toStringIndented(1) +
                "\n" + indent + "  instrument= " + this.instrument +
                "\n" + indent + "  activeBooking= " + this.activeBooking +
                "\n" + indent + "  bookingStart= " + this.bookingStart +
                "\n" + indent + "  bookingEnd= " + this.bookingEnd +
                "\n" + indent + "  deleted= " + this.deleted +
                "\n" + indent + "}";
    }
}
