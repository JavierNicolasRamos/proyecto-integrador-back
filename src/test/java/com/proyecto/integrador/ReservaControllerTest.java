package com.proyecto.integrador;

import com.proyecto.integrador.controller.ReservaController;
import com.proyecto.integrador.dto.ReservaDto;
import com.proyecto.integrador.entity.Reserva;
import com.proyecto.integrador.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
@SpringBootTest
public class ReservaControllerTest {

    @InjectMocks
    private ReservaController reservaController;

    @Mock
    private ReservaService reservaService;

    @Test
    public void testListarReservas() {

        List<Reserva> reservas = new ArrayList<>();

        Mockito.when(reservaService.listarReservas()).thenReturn(reservas);

        List<Reserva> resultado = reservaController.listarReservas();

        assertEquals(reservas, resultado);
    }

    @Test
    public void testObtenerReserva() {

        Long id = 1L;

        Reserva reserva = new Reserva();

        Mockito.when(reservaService.obtenerReserva(id)).thenReturn(reserva);

        Reserva resultado = reservaController.obtenerReserva(id);

        assertEquals(reserva, resultado);
    }

    @Test
    public void testCrearReserva() {
        ReservaDto reservaDto = new ReservaDto();
        Reserva reservaCreada = new Reserva();

        Mockito.when(reservaService.crearReserva(reservaDto)).thenReturn(reservaCreada);

        Reserva resultado = reservaController.crearReserva(reservaDto);


        assertEquals(reservaCreada, resultado);
    }

    @Test
    public void testActualizarReserva() {
        Long id = 1L;

        ReservaDto reservaDto = new ReservaDto();
        Reserva reservaActualizada = new Reserva();


        Mockito.when(reservaService.actualizarReserva(id, reservaDto)).thenReturn(reservaActualizada);

        Reserva resultado = reservaController.actualizarReserva(id, reservaDto);

        assertEquals(reservaActualizada, resultado);
    }

    @Test
    public void testEliminarReserva() {
        Long id = 1L;

        reservaController.eliminarReserva(id);

        Mockito.verify(reservaService).eliminarReserva(id);
    }
}

