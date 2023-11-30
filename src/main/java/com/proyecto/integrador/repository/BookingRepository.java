package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Booking;
import com.proyecto.integrador.entity.Instrument;
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

    @Query(" SELECT b FROM Booking b " +
           " WHERE b.instrument.id = :id " +
           " AND b.deleted = false " +
           " AND ((b.bookingStart BETWEEN :start AND :end) OR (b.bookingEnd BETWEEN :start AND :end))")
    List<Booking> findOverlappingBookings(@Param("id") Long id, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.instrument.id = :id " +
            "AND b.deleted = false " +
            "AND ((b.bookingStart BETWEEN :start AND :end) OR (b.bookingEnd BETWEEN :start AND :end))")
    boolean hasOverlappingBookings(@Param("id") Long id, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT b.bookingStart, b.bookingEnd FROM Booking b WHERE b.instrument.id = :instrumentId")
    List<Object[]> findOccupiedDatesByInstrumentId(@Param("instrumentId") Long instrumentId);
}
