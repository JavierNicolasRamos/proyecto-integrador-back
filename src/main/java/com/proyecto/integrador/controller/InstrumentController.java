package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instruments")
public class InstrumentController {
    @Autowired
    private InstrumentService instrumentService;

    @PostMapping
    public Instrument createInstrument(@RequestBody InstrumentDto instrument/*,@RequestPart(value = "file") MultipartFile file*/) {
        return instrumentService.createInstrument(instrument);//TODO: Pasar como parametro file
    }

    @GetMapping
    public List<Instrument> getTenInstruments() {
        return instrumentService.getTenInstruments();
    }

    @GetMapping("/paginated")
    public Page<Instrument>getAll(Pageable pageable){
        return instrumentService.getAll(pageable);
    }

    @GetMapping("/{id}")
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