package com.proyecto.integrador.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.entity.Imagen;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.repository.CategoriaRepository;
import com.proyecto.integrador.repository.ImagenRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

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
    private ImagenRepository imagenRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Bean
    @Order(1)
    public List<Map<String, Object>> dataSeed() throws IOException {
        InputStream inputStream = new ClassPathResource("dataSeed.json").getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> data = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});

        return data;
    }

    @Bean
    @Order(2)
    @Transactional
    public List<Instrumento> crearInstrumentos(@Qualifier("dataSeed") List<Map<String, Object>> dataSeed) {
        List<Instrumento> instrumentos = new ArrayList<>();

        for (Map<String, Object> instrumentoData : dataSeed) {
            String nombre = (String) instrumentoData.get("nombre");
            Double puntuacion = (Double) instrumentoData.get("puntuacion");
            String detalle = (String) instrumentoData.get("detalle");
            Map<String, Object> categoriaData = (Map<String, Object>) instrumentoData.get("categoria");
            String categoriaDescripcion = (String) categoriaData.get("descripcion");

            Optional<Instrumento> existeInstrumento = instrumentoRepository.getByNombre(nombre);
            if (!existeInstrumento.isPresent()) {
                Instrumento instrumento = new Instrumento();
                Optional<Categoria> existeCategoria = categoriaRepository.findByDescripcion(categoriaDescripcion);
                if (!existeCategoria.isPresent()) {
                    Categoria categoria = new Categoria();
                    categoria.setDescripcion(categoriaDescripcion);
                    categoria.setEliminado(false);
                    this.categoriaRepository.save(categoria);
                    instrumento.setCategoria(categoria);
                } else {
                    instrumento.setCategoria(existeCategoria.get());
                }

                instrumento.setNombre(nombre);
                instrumento.setFechaCarga(LocalDate.now());
                instrumento.setFechaUpdate(LocalDate.now());
                instrumento.setPuntuacion(puntuacion);
                instrumento.setDetalle(detalle);
                instrumento.setEliminado(false);
                instrumento.setDisponible(true);
                instrumentoRepository.save(instrumento);

                List<String> imagenes = (List<String>) instrumentoData.get("imagen");
                for (String imagenUrl : imagenes) {
                    Imagen imagen = new Imagen();
                    imagen.setImagen(imagenUrl);
                    imagen.setEliminado(false);
                    imagenRepository.save(imagen);
                }
                instrumentos.add(instrumento);
            }
        }

        return instrumentos;
    }

}
