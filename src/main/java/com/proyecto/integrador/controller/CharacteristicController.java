package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.CharacteristicDto;
import com.proyecto.integrador.entity.Characteristic;
import com.proyecto.integrador.service.CharacteristicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characteristic")
public class CharacteristicController {
    @Autowired
    private CharacteristicService characteristicService;

    @PostMapping
    public ResponseEntity<Characteristic> createCharacteristic(@RequestBody CharacteristicDto characteristicDto){
        return ResponseEntity.ok(characteristicService.createCharacteristic(characteristicDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Characteristic> editCharacteristic(@PathVariable("id") Long id, @RequestBody CharacteristicDto characteristic) {
        return ResponseEntity.ok(characteristicService.editCharacteristic(id, characteristic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCharacteristic(@PathVariable("id") Long id) {
        characteristicService.deleteCharacteristic(id);
        return ResponseEntity.ok("La characterística con ID " + id + " se eliminó de manera exitosa");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Characteristic>> listCharacteristic() {
        return ResponseEntity.ok(characteristicService.listCharacteristic());
    }
}
