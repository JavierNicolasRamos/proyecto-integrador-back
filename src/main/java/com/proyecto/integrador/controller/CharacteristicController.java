package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.CharacteristicDto;
import com.proyecto.integrador.entity.Characteristic;
import com.proyecto.integrador.service.CharacteristicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characteristic")
public class CharacteristicController {
    @Autowired
    private CharacteristicService characteristicService;

    @PostMapping
    public Characteristic createCharacteristic(@RequestBody CharacteristicDto characteristicDto){
        return characteristicService.createCharacteristic(characteristicDto);
    }
    @PutMapping("/{id}")
    public Characteristic editCharacteristic(@PathVariable Long id, @RequestBody CharacteristicDto characteristic) {
        return characteristicService.editCharacteristic(id, characteristic);
    }

    @DeleteMapping("/{id}")
    public void deleteCharacteristic(@PathVariable Long id) {
        characteristicService.deleteCharacteristic(id);
    }


    @GetMapping("/list")
    public List<Characteristic> listCharacteristic() {
        return characteristicService.listCharacteristic();
    }






}
