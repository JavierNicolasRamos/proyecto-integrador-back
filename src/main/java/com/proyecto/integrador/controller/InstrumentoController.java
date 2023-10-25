package com.proyecto.integrador.controller;

// Asumiendo que tienes una clase Instrumento
import com.proyecto.integrador.dto.InstrumentoDto;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.service.InstrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrumentos")
public class InstrumentoController {
    @Autowired
    private InstrumentoService instrumentoService;

    @PostMapping
    public Instrumento crearInstrumento(@RequestBody InstrumentoDto instrumento) {
        return instrumentoService.crearInstrumento(instrumento);
    }

    @GetMapping
    public Page<Instrumento> obtenerDiezInstrumentos(Pageable pageable) {
        return instrumentoService.obtenerDiezInstrumentos(pageable);
    }

    @GetMapping("/paginado")
    public Page<Instrumento>getAll(Pageable pageable){
        return instrumentoService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Instrumento obtenerInstrumentoPorId(@PathVariable Long id) {
        return instrumentoService.obtenerInstrumentoPorId(id);
    }

    @PutMapping("/{id}")
    public Instrumento actualizarInstrumento(@PathVariable Long id, @RequestBody InstrumentoDto instrumento) {
        return instrumentoService.actualizarInstrumento(id, instrumento);
    }

    @DeleteMapping("/{id}")
    public void eliminarInstrumento(@PathVariable Long id) {
        instrumentoService.eliminarInstrumento(id);
    }


}


