package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.user.email = :email AND b.instrument.id = :id")
    Optional<List<Booking>> findByUserEmailAndInstrumentId(@Param("email") String email, @Param("id") Long id);

    List<Booking> findByBookingEndAndDeletedIsFalse(LocalDate bookingEnd);
}
