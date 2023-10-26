package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.CategoriaDto;
import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.service.CategoriaService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    //Retornar el objeto (categoria) completo no la response entity
    //
    @PostMapping
    public Categoria crearCategoria(@RequestBody CategoriaDto categoriaDto) {
        return categoriaService.crearCategoria(categoriaDto);
    }

    @GetMapping("descripcion/{descripcion}")
    //Buscar como pedir el not null
    public Categoria buscarCategoriaPorDescipcion(@PathVariable String descripcion){
        //Falta agregar que descripcion sea diferente de nula
        return categoriaService.buscarCategoriaPorDescripcion(descripcion);
    }

    @GetMapping("/{id}") //Cambiar por "/{id}"
    public Long contarInstrumentosPorCategoria(@PathVariable Long id){
        return categoriaService.contarInstrumentosPorCategoria(id);
    }

    @DeleteMapping("/{id}")//Cambiar por "/{id}"
    public void eliminarInstrumentosPorCategoria(@PathVariable Long id){
    }

    @GetMapping
    public List<Categoria> listarCategorias(){
        return categoriaService.listarCategorias();
    }
}
