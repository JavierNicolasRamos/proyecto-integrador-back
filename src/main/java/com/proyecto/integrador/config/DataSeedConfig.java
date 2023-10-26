package com.proyecto.integrador.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.dto.InstrumentoDto;
import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.DuplicateInstrumentException;
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import com.proyecto.integrador.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
public class DataSeedConfig {

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Bean
    @Order(1)
    public Map<String, Object> dataSeed() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/dataSeed.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> data = objectMapper.readValue(inputStream, Map.class);

        return data;
    }

    @Bean
    @Order(2)
    public List<Instrumento> crearInstrumentos(@Qualifier("dataSeed") Map<String, Object> dataSeed) {
        List<Instrumento> instrumentos = new ArrayList<>();

        for (Map.Entry<String, Object> entry : dataSeed.entrySet()) {
            Map<String, Object> instrumentoData = (Map<String, Object>) entry.getValue();

            String nombre = (String) instrumentoData.get("nombre");
            Double puntuacion = (Double) instrumentoData.get("puntuacion");
            String detalle = (String) instrumentoData.get("detalle");

            Map<String, Object> categoriaData = (Map<String, Object>) instrumentoData.get("categoria");
            String categoriaDescripcion = (String) categoriaData.get("descripcion");

            Optional<Instrumento> existeInstrumento = instrumentoRepository.getByNombre(nombre);
            if (existeInstrumento.isPresent()) {
                throw new DuplicateInstrumentException("Ya existe un instrumento con el mismo nombre: " + nombre);
            }

            Instrumento instrumento = new Instrumento();
            Optional<Categoria> existeCategoria = categoriaRepository.findByDescripcion(categoriaDescripcion);
            if (!existeCategoria.isPresent()){
                Categoria categoria = new Categoria();
                categoria.setDescripcion(categoriaDescripcion);
                this.categoriaRepository.save(categoria);
                instrumento.setCategoria(categoria);
            }
            else {
                instrumento.setCategoria(existeCategoria.get());
            }

            instrumento.setNombre(nombre);
            instrumento.setFechaCarga(LocalDate.now());
            instrumento.setFechaUpdate(LocalDate.now());
            instrumento.setPuntuacion(puntuacion);
            instrumento.setDetalle(detalle);
            instrumento.setDisponible(true);

            instrumentoRepository.save(instrumento);
            this.imagenService.guardarImagenesInstrumento(instrumento);

            instrumentos.add(instrumento);
        }

        return instrumentos;
    }

}
