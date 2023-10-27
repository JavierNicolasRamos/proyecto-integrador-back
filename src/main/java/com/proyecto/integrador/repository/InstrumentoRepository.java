package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.entity.Instrumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.List;

public interface InstrumentoRepository extends JpaRepository<Instrumento, Long> {

    @Query( value = "SELECT * FROM instrumentos WHERE nombre = :nombre", nativeQuery = true)
    Optional<Instrumento> getByNombre(@Param("nombre") String nombre);

    @Query(value = "SELECT * FROM instrumentos WHERE eliminado = false ORDER BY RAND() LIMIT 10;", nativeQuery = true)
    List<Instrumento> findRandomInstruments();

    @Query(value = "SELECT * FROM instrumentos WHERE eliminado = false", nativeQuery = true)
    Page<Instrumento> getAll(Pageable pageable);

    @Query(value = "COUNT(*) FROM instrumentos WHERE categoria_id = :categoria_id AND WHERE eliminado  = 0", nativeQuery = true)
    Long countAllByCategoriaAndEliminado(@Param("categoria_id") Long id);

    List<Instrumento> findAllByCategoria(Categoria categoria);

    @Query(value = "SELECT * FROM instrumentos WHERE id = :id AND eliminado = false", nativeQuery = true)
    Optional<Instrumento> buscarPorId(@Param("id") Long id);
}
