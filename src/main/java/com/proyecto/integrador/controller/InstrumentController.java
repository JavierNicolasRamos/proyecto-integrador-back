package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/instruments")
public class InstrumentController {
    @Autowired
    private InstrumentService instrumentService;

    @PostMapping
    public Instrument createInstrument(@RequestPart("instrument") InstrumentDto instrument, @RequestPart("images") List<MultipartFile> images) {
        return instrumentService.createInstrument(instrument, images);
    }

    @PostMapping("/createimages")
    public ResponseEntity<Instrument> createImagesInstrument(@RequestPart("id") Long id, @RequestPart("images")List<MultipartFile> images){
        return ResponseEntity.ok(this.instrumentService.createImagesInstrument(id, images));
    }

    @GetMapping
    public List<Instrument> getTenInstruments() {
        return instrumentService.getTenInstruments();
    }

    @GetMapping("/paginated")
    public Page<Instrument>getAll(Pageable pageable){
        return instrumentService.getAll(pageable);
    }

    @GetMapping("/id/{id}")
    public Instrument getInstrumentById(@PathVariable Long id) {
        return instrumentService.getInstrumentById(id);
    }

    @PutMapping("/{id}")
    public Instrument updateInstrument(@PathVariable Long id, @RequestBody InstrumentDto instrument) {
        return instrumentService.updateInstrument(id, instrument);
    }

    @DeleteMapping("/{id}")
    public void deleteInstrument(@PathVariable Long id) {
        instrumentService.deleteInstrument(id);
    }

    @GetMapping("/{name}")
    public Page<Instrument> getName(@PathVariable String name, Pageable pageable){
        return instrumentService.getName(name, pageable);
    }


}