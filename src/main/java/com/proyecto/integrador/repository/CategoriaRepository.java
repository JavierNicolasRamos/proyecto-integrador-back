package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByDescripcion(String descripcion);
}
