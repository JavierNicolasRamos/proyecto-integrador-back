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

    @PostMapping
    public Categoria crearCategoria(@RequestBody CategoriaDto categoriaDto) {
        return categoriaService.crearCategoria(categoriaDto);
    }

    @GetMapping("descripcion/{descripcion}")
    public Categoria buscarCategoriaPorDescipcion(@PathVariable String descripcion){
        return categoriaService.buscarCategoriaPorDescripcion(descripcion);
    }

    @GetMapping("contarinstrumentos/{id}")
    public Long contarInstrumentosPorCategoria(@PathVariable Long id){
        return categoriaService.contarInstrumentosPorCategoria(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarInstrumentosPorCategoria(@PathVariable Long id){
        this.categoriaService.eliminarInstrunmentosPorCategoria(id);
    }

    @GetMapping
    public List<Categoria> listarCategorias(){
        return categoriaService.listarCategorias();
    }
}
