package com.proyecto.integrador.repository;

import com.proyecto.integrador.dto.CategoriaDto;
import com.proyecto.integrador.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findAllByEliminado(Boolean eliminado);

    @Query(value = "SELECT * FROM categorias WHERE descripcion = :descripcion AND eliminado = false", nativeQuery = true)
    Optional<Categoria> findByDescripcion(String descripcion);

    @Query(value = "SELECT * FROM categorias WHERE id = :id AND eliminado = false", nativeQuery = true)
    Optional<Categoria> buscarPorId(@Param("id") Long id);

    Categoria updateCategoriaById(Long id, Categoria categoria);
}
