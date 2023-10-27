package com.proyecto.integrador.repository;

import com.proyecto.integrador.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findAllByEliminado(Boolean eliminado);

    Optional<Categoria> findByDescripcion(String descripcion);
}