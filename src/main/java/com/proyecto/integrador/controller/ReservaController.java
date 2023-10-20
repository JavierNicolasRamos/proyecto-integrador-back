package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.ReservaDto;
import com.proyecto.integrador.entity.Reserva;
import com.proyecto.integrador.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public List<Reserva> listarReservas() {
        return reservaService.listarReservas();
    }

    @GetMapping("/{id}")
    public Reserva obtenerReserva(@PathVariable Long id) {
        return reservaService.obtenerReserva(id);
    }

    @PostMapping
    public Reserva crearReserva(@RequestBody ReservaDto reservaDto) {
        return reservaService.crearReserva(reservaDto);
    }

    @PutMapping("/{id}")
    public Reserva actualizarReserva(@PathVariable Long id, @RequestBody ReservaDto reservaDto) {
        reservaDto.setId(id);
        return reservaService.actualizarReserva(id, reservaDto);
    }

    @DeleteMapping("/{id}")
    public void eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
    }
}
