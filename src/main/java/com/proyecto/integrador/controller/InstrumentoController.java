package com.proyecto.integrador.controller;

// Asumiendo que tienes una clase Instrumento
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.InstrumentoNotFoundException;
import com.proyecto.integrador.service.InstrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrumentos")
public class InstrumentoController {
    @Autowired
    private InstrumentoService instrumentoService;

    @PostMapping
    public Instrumento crearInstrumento(@RequestBody Instrumento instrumento) {



        //excepciones
        boolean errorOcurre = false;
        if (errorOcurre) {
            throw new InstrumentoNotFoundException("No se pudo crear el instrumento");
        }
        return instrumentoService.crearInstrumento(instrumento);

    }

    @GetMapping
    public List<Instrumento> obtenerDiezInstrumentos() {


        //excepciones
        if (instrumentos.isEmpty()) {
            throw new InstrumentosNotFoundException("No se encontraron instrumentos");
        }
        return instrumentoService.obtenerDiezInstrumentos();
    }

    @GetMapping("/{id}")
    public Instrumento obtenerInstrumentoPorId(@PathVariable Long id) {

        //excepciones
        if (instrumento == null) {
            throw new InstrumentoNotFoundException("Instrumento no encontrado con ID: " + id);
        }
        return instrumentoService.obtenerInstrumentoPorId(id);
    }

    @PutMapping("/{id}")
    public Instrumento actualizarInstrumento(@PathVariable Long id, @RequestBody Instrumento instrumento) {
        return instrumentoService.actualizarInstrumento(id, instrumento);
    }

    @DeleteMapping("/{id}")
    public void eliminarInstrumento(@PathVariable Long id) {
        instrumentoService.eliminarInstrumento(id);
    }


}


