package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {
    @Query(value = "SELECT * FROM caracteristicas WHERE nombre = :nombre", nativeQuery = true)
    Optional<Caracteristica> findByNombre(@Param("nombre") String nombre);
}
