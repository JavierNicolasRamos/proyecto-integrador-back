package com.proyecto.integrador;

import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.service.InstrumentoService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InstrumentoController {

    @InjectMocks
    private InstrumentoController instrumentoController;

    @Mock
    private InstrumentoService instrumentoService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCrearInstrumento() {
        Instrumento instrumento = new Instrumento();
        when(instrumentoService.crearInstrumento(instrumento)).thenReturn(instrumento);

        ResponseEntity<Instrumento> response = instrumentoController.crearInstrumento(instrumento);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(instrumento, response.getBody());

        verify(instrumentoService, times(1)).crearInstrumento(instrumento);
    }

    @Test
    public void testObtenerDiezInstrumentos() {
        List<Instrumento> instrumentos = new ArrayList<>();
        when(instrumentoService.obtenerDiezInstrumentos()).thenReturn(instrumentos);

        List<Instrumento> result = instrumentoController.obtenerDiezInstrumentos();

        assertEquals(instrumentos, result);

        verify(instrumentoService, times(1)).obtenerDiezInstrumentos();
    }


}
