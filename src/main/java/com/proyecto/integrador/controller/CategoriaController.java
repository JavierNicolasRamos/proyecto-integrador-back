package com.proyecto.integrador.controller;

import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Object> crearCategoria(@RequestBody Categoria categoria) {
        Optional<Categoria>  categoriaOptionalDescripcion = categoriaService.buscarCategoriaPorDescripcion(categoria.getDescripcion());

        //Verifico que la categoria no exista
        if(categoriaOptionalDescripcion.isPresent()){
            //LOGGER.info("La catergoria ya existe");
            return ResponseEntity.badRequest().build();
        }else{
            //LOGGER.info("Categoria guardada correctamente")
            return ResponseEntity.ok(categoriaService.crearCategoria(categoria));
        }

    }

}
