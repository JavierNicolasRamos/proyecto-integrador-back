package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
