package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Image;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query(value = "SELECT * FROM images WHERE id = :id AND deleted = false", nativeQuery = true)
    @NotNull
    Optional<Image> findById(@Param("id") @NotNull Long id);
}
