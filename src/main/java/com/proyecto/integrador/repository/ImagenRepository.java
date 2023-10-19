package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {
}
