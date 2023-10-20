import com.proyecto.integrador.controller.InstrumentoController;
import com.proyecto.integrador.dto.InstrumentoDto;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.service.InstrumentoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class InstrumentoControllerTest {

    @InjectMocks
    private InstrumentoController instrumentoController;

    @Mock
    private InstrumentoService instrumentoService;

    @Test
    public void testCrearInstrumento() {

        InstrumentoDto instrumentoDto = new InstrumentoDto();
        Mockito.when(instrumentoService.crearInstrumento(instrumentoDto)).thenReturn(new Instrumento());

        Instrumento resultado = instrumentoController.crearInstrumento(instrumentoDto);

        assertNotNull(resultado);
    }

    @Test
    public void testObtenerDiezInstrumentos() {

        List<Instrumento> instrumentos = new ArrayList<>();

        Mockito.when(instrumentoService.obtenerDiezInstrumentos()).thenReturn(instrumentos);


        List<Instrumento> resultado = instrumentoController.obtenerDiezInstrumentos();

        assertNotNull(resultado);
        assertEquals(instrumentos.size(), resultado.size());
    }

    @Test
    public void testObtenerInstrumentoPorId() {

        Long id = 1L;

        Instrumento instrumento = new Instrumento();
        Mockito.when(instrumentoService.obtenerInstrumentoPorId(id)).thenReturn(instrumento);


        Instrumento resultado = instrumentoController.obtenerInstrumentoPorId(id);

        assertNotNull(resultado);
    }

    @Test
    public void testActualizarInstrumento() {

        Long id = 1L;

        InstrumentoDto instrumentoDto = new InstrumentoDto();

        Mockito.when(instrumentoService.actualizarInstrumento(id, instrumentoDto)).thenReturn(new Instrumento());

        Instrumento resultado = instrumentoController.actualizarInstrumento(id, instrumentoDto);


        assertNotNull(resultado);
    }

    @Test
    public void testEliminarInstrumento() {

        Long id = 1L;

        instrumentoController.eliminarInstrumento(id);

        Mockito.verify(instrumentoService).eliminarInstrumento(id);
    }
}

