package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instruments")
public class InstrumentController {
    @Autowired
    private InstrumentService instrumentService;

    @PostMapping
    public ResponseEntity<Instrument> createInstrument(@RequestBody InstrumentDto instrument/*,@RequestPart(value = "file") MultipartFile file*/) {
        return ResponseEntity.ok(instrumentService.createInstrument(instrument));//TODO: Pasar como parametro file
    }

    @GetMapping
    public ResponseEntity<List<Instrument>> getTenInstruments() {
        return ResponseEntity.ok(instrumentService.getTenInstruments());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Instrument>>getAll(Pageable pageable){
        return ResponseEntity.ok(instrumentService.getAll(pageable));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Instrument> getInstrumentById(@PathVariable Long id) {
        return ResponseEntity.ok(instrumentService.getInstrumentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instrument> updateInstrument(@PathVariable Long id, @RequestBody InstrumentDto instrument) {
        return ResponseEntity.ok(instrumentService.updateInstrument(id, instrument));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInstrument(@PathVariable Long id) {
        instrumentService.deleteInstrument(id);
        return ResponseEntity.ok("Se elimino el instrumento con ID: " + id + " de manera exitosa");
    }

    @GetMapping("/{name}")
    public ResponseEntity<Page<Instrument>> getName(@PathVariable String name, Pageable pageable){
        return ResponseEntity.ok(instrumentService.getName(name, pageable));
    }
}