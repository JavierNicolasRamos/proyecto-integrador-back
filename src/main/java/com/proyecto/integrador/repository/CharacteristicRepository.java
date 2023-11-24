package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {
    @Query(value = "SELECT * FROM characteristics WHERE name = :name", nativeQuery = true)
    Optional<Characteristic> findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM characteristics WHERE deleted = false", nativeQuery = true)
    List<Characteristic> getAll();

}
