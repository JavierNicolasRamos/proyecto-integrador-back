package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByDescripcion(String descripcion);

    //ELIMINAR
    @Query(value = "UPDATE CATEGORIAS SET eliminado = 1 WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);

}
