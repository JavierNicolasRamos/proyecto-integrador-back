package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Instrument;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.List;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    @Query( value = "SELECT * FROM instruments WHERE name = :name", nativeQuery = true)
    Optional<Instrument> getByName(@Param("name") String name);

    @Query(value = "SELECT * FROM instruments WHERE name LIKE %:name% AND deleted = false", nativeQuery = true)
    Page<Instrument> getName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM instruments WHERE deleted = false ORDER BY RAND() LIMIT 10;", nativeQuery = true)
    List<Instrument> findRandomInstruments();

    @Query(value = "SELECT * FROM instruments WHERE deleted = false", nativeQuery = true)
    Page<Instrument> getAll(Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM instruments WHERE category_id = :id AND deleted  = false", nativeQuery = true)
    Long countAllByCategory(@Param("id") Long id);

    List<Instrument> findAllByCategory(Category category, Boolean deleted);

    @Query(value = "SELECT * FROM instruments WHERE id = :id AND deleted = false", nativeQuery = true)
    @NotNull
    Optional<Instrument> findById(@Param("id") @NotNull Long id);
}
