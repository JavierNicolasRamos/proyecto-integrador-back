package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.CategoriaDto;
import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.service.CategoriaService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Object> crearCategoria(@RequestBody CategoriaDto categoriaDto) {
        return ResponseEntity.ok(categoriaService.crearCategoria(categoriaDto));
    }

    @GetMapping("descripcion/{descripcion}")
    public ResponseEntity<Object> buscarCategoriaPorDescipcion(@PathVariable String descripcion){
        return ResponseEntity.ok(categoriaService.buscarCategoriaPorDescripcion(descripcion));
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Object> contarInstrumentosPorCategoria(@PathVariable Long id){
        return ResponseEntity.ok(categoriaService.contarInstrumentosPorCategoria(id));
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<Object> eliminarInstrumentosPorCategoria(@PathVariable Long id){
        return ResponseEntity.ok(categoriaService.eliminarInstrunmentosPorCategoria(id));
    }
}
