package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Category;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM categories WHERE deleted = :deleted", nativeQuery = true)
    @NotNull
    List<Category> findAll(Boolean deleted);

    @Query(value = "SELECT * FROM categories WHERE description = :description AND deleted = false", nativeQuery = true)
    @NotNull
    Optional<Category> findByName(String description);

    @Query(value = "SELECT * FROM categories WHERE id = :id AND deleted = false", nativeQuery = true)
    @NotNull
    Optional<Category> findById(@Param("id") @NotNull Long id);

}
