package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Imagen;
import com.proyecto.integrador.entity.Instrumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    @Query(value = "SELECT * FROM imagenes WHERE id = :id AND eliminado = false", nativeQuery = true)
    Optional<Imagen> buscarPorId(@Param("id") Long id);
}
