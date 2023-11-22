package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRespository extends JpaRepository<Review, Long> {

}
