package com.proyecto.integrador.controller;


import com.amazonaws.services.dynamodbv2.document.Page;
import com.proyecto.integrador.dto.CaracteristicaDto;
import com.proyecto.integrador.entity.Caracteristica;
import com.proyecto.integrador.service.CaracteristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/caracteristica")
public class CaracteristicaController {
    @Autowired
    private CaracteristicaService caracteristicaService;

    @PostMapping
    public Caracteristica crearCaracteristica(@RequestBody CaracteristicaDto caracteristicaDto){
        return caracteristicaService.crearCaracteristica(caracteristicaDto);
    }
    @PutMapping("/{id}")
    public Caracteristica editarCaracteristica(@PathVariable Long id, @RequestBody CaracteristicaDto caracteristica) {
        return caracteristicaService.editarCaracteristica(id, caracteristica);
    }

    @DeleteMapping("/{id}")
    public void eliminarCaracteristica(@PathVariable Long id) {
        caracteristicaService.eliminarCaracteristica(id);
    }


    @GetMapping("/listar")
    public List<Caracteristica> listarCaracteristica() {
        return caracteristicaService.listarCaracteristica();
    }






}
