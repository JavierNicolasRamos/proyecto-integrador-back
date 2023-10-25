package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Instrumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InstrumentoRepository extends JpaRepository<Instrumento, Long> {

    @Query( value = "SELECT * FROM INSTRUMENTOS WHERE NOMBRE = :nombre", nativeQuery = true)
    Optional<Instrumento> getByNombre(@Param("nombre") String  nombre);

    @Query(value = "SELECT * FROM instrument WHERE eliminado = false ORDER BY RAND() LIMIT 10", nativeQuery = true)
    Page<Instrumento> findRandomInstruments(Pageable pageable);

    @Query(value = "SELECT * FROM instrument WHERE eliminado = false", nativeQuery = true)
    Page<Instrumento> getAll(Pageable pageable);
}
