package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByDescripcion(String descripcion);
    Long countAllByDescripcion(String descripcion);

    //Eliminacion con columna logica de la categoria
    @Query(value = "INSERT INTO CATEGORIAS (elimando) VALUE (1) WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}