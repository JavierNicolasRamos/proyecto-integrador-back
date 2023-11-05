package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.CategoriaDto;
import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.service.CategoriaService;
import com.proyecto.integrador.service.InstrumentoService;
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
    public void deleteCategoria(@PathVariable Long id){
        this.categoriaService.deleteCategoria(id);
    }

    @GetMapping("/list")
    public List<Categoria> listarCategorias(){
        return categoriaService.listarCategorias();
    }

    @PutMapping
    public Categoria updateCategory(@PathVariable CategoriaDto categoria){
        return categoriaService.updateCategory(categoria);
    }

    @GetMapping("/instrumentos")
    public List<Instrumento> getInstrumentsByCategories(@RequestBody List<Categoria> categoryList){
        return categoriaService.getInstrumentsByCategories(categoryList);
    }
}
