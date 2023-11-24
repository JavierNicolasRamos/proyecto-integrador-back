package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRespository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.boyer.email = :email AND r.instrument.id = :instrumentId")
    Optional<Review> findByBoyerEmailAndInstrumentId(@Param("email") String email, @Param("instrumentId") Long instrumentId);

}
