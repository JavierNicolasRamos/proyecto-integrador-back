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

    @Query( value = "SELECT * FROM INSTRUMENTOS WHERE NOMBRE = :nombre", nativeQuery = true)
    Optional<Instrumento> getByNombre(@Param("nombre") String  nombre);

    @Query(value = "SELECT * FROM INSTRUMENTOS WHERE eliminado = false", countQuery = "SELECT count(*) FROM INSTRUMENTOS WHERE eliminado = false", nativeQuery = true)
    Page<Instrumento> findRandomInstruments(Pageable pageable);

    @Query(value = "SELECT * FROM INSTRUMENTOS WHERE eliminado = false", nativeQuery = true)
    Page<Instrumento> getAll(Pageable pageable);
    
    @Query(value = "SELECT * FROM INSTRUMENTOS WHERE id = :id AND eliminado = false", nativeQuery = true)
    Optional<Instrumento> buscarPorId(@Param("id") Long id);

    @Query(value = "COUNT(*) FROM instrumentos WHERE categoria_id = :categoria_id AND WHERE eliminado  = 0", nativeQuery = true)
    Long countAllByCategoriaAndEliminado(@Param("categoria_id") Long id);

    List<Instrumento> findAllByCategoria(Categoria categoria);
}
