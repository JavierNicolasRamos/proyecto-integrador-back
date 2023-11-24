package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRespository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.boyer.email = :email AND r.instrument.id = :instrumentId")
    Optional<Review> findByBoyerEmailAndInstrumentId(@Param("email") String email, @Param("instrumentId") Long instrumentId);

    @Query("SELECT r FROM Review r WHERE r.boyer.id = :id")
    List<Review> findByBoyer(@Param("id") Long id);

    @Query("SELECT r FROM Review r WHERE r.instrument.id = :id")
    List<Review> findByInstrument(@Param("id") Long id);
}
