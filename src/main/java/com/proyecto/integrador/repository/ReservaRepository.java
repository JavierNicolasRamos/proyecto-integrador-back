package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

}
