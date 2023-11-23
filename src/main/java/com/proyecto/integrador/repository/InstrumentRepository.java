package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.User;
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

    @Query(value = "SELECT * FROM instruments WHERE category_id = :category AND deleted = false", nativeQuery = true)
    List<Instrument> findAllByCategory(Long category);

    @Query(value = "SELECT * FROM instruments WHERE id = :id AND deleted = false", nativeQuery = true)
    @NotNull
    Optional<Instrument> findById(@Param("id") @NotNull Long id);

    @Query(value = "SELECT instruments.id, instruments.available,instruments.deleted, instruments.detail, instruments.name, instruments.review_count, instruments.score, instruments.update_date, instruments.upload_date, instruments.category_id, instruments.seller_id" +
            "FROM instruments " +
            "INNER JOIN instruments_users ON instruments_users.instruments_id = instruments.id " +
            "INNER JOIN users ON instruments_users.users_id = users.id " +
            "WHERE instrument.deleted = false AND users.email :email ", nativeQuery = true)
    Optional<List<Instrument>> findFavouritesByUserEmail(@Param("email") @NotNull String email);

    Optional<List<Instrument>> findAllByUserAndDeleted(User user, Boolean deleted);

}
