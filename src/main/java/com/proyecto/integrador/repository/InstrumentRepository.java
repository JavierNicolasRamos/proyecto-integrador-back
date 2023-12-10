package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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

    @Query(value = "SELECT * FROM instruments WHERE category_id = :category AND deleted = false", nativeQuery = true)
    List<Instrument> findAllByCategory(Long category);

    @Query(value = "SELECT * FROM instruments WHERE id = :id AND deleted = false", nativeQuery = true)
    @NotNull
    Optional<Instrument> findById(@Param("id") @NotNull Long id);


    @Query("SELECT DISTINCT i FROM Instrument i " +
            "WHERE (LOWER(i.name) LIKE LOWER(concat('%', :partialName, '%')) " +
            "OR LOWER(i.category.name) LIKE LOWER(concat('%', :partialName, '%'))) " +
            "AND i.deleted = false")
    List<Instrument> findByPartialName(
            @Param("partialName") String partialName);

    @Query("SELECT i FROM Instrument i JOIN i.characteristics c WHERE c.id = :characteristicId AND i.deleted = false")
    List<Instrument> findByCharacteristicsIdAndDeletedIsFalse(@Param("characteristicId") Long characteristicId);

    @Query(" SELECT i FROM Instrument i " +
            "LEFT JOIN i.bookings b " +
            "WHERE (b.bookingStart > :endDate OR b.bookingEnd < :startDate) OR b.id IS NULL")
    List<Instrument> findAvailableInstruments(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT i FROM Instrument i " +
            "LEFT JOIN i.bookings b " +
            "WHERE ((LOWER(i.name) LIKE LOWER(concat('%', :partialName, '%')) " +
            "OR LOWER(i.category.name) LIKE LOWER(concat('%', :partialName, '%'))) " +
            "AND i.deleted = false) " +
            "AND ((b.bookingStart > :endDate OR b.bookingEnd < :startDate) OR b.id IS NULL)")
    List<Instrument> findInstrumentsByNameAndAvailability(
            @Param("partialName") String partialName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
