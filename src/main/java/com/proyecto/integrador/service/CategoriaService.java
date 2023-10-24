package com.proyecto.integrador.service;

import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria crearCategoria(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> buscarCategoriaPorDescripcion(String descripcion){
        return categoriaRepository.findByDescripcion(descripcion);
    }

}

