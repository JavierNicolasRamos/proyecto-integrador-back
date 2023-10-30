package com.proyecto.integrador.controller;


import com.proyecto.integrador.dto.CaracteristicasDto;
import com.proyecto.integrador.entity.Caracteristicas;
import com.proyecto.integrador.service.CaracteristicasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caracteristicas")
public class CaracteristicasController {
    @Autowired
    private CaracteristicasService caracteristicasService;

    @PostMapping
    public Caracteristicas crearCaracteristica(@RequestBody CaracteristicasDto caracteristicasDto){
        return caracteristicasService.crearCaracteristica(caracteristicasDto);
    }
    @PutMapping("/{id}")
    public Caracteristicas editarCaracteristica(@PathVariable Long id, @RequestBody CaracteristicasDto caracteristica) {
        return caracteristicasService.editarCaracteristica(id, caracteristica);
    }

    @DeleteMapping("/{id}")
    public void eliminarCaracteristica(@PathVariable Long id) {
        caracteristicasService.eliminarCaracteristica(id);
    }


}
