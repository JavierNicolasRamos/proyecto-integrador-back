package com.proyecto.integrador.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Image;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.repository.CategoryRepository;
import com.proyecto.integrador.repository.ImageRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
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
    private InstrumentRepository instrumentRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
    public List<Instrument> crearInstrumentos(@Qualifier("dataSeed") List<Map<String, Object>> dataSeed) {
        List<Instrument> instruments = new ArrayList<>();

        for (Map<String, Object> instrumentoData : dataSeed) {
            String nombre = (String) instrumentoData.get("nombre");
            Double puntuacion = (Double) instrumentoData.get("puntuacion");
            String detalle = (String) instrumentoData.get("detalle");
            Map<String, Object> categoriaData = (Map<String, Object>) instrumentoData.get("categoria");
            String categoriaDescripcion = (String) categoriaData.get("descripcion");

            Optional<Instrument> existeInstrumento = instrumentRepository.getByName(nombre);
            if (!existeInstrumento.isPresent()) {
                Instrument instrument = new Instrument();
                Optional<Category> existeCategoria = categoryRepository.findByName(categoriaDescripcion);
                if (!existeCategoria.isPresent()) {
                    Category category = new Category();
                    category.setName(categoriaDescripcion);
                    category.setDeleted(false);
                    this.categoryRepository.save(category);
                    instrument.setCategory(category);
                } else {
                    instrument.setCategory(existeCategoria.get());
                }

                instrument.setName(nombre);
                instrument.setUploadDate(LocalDate.now());
                instrument.setUpdateDate(LocalDate.now());
                instrument.setScore(puntuacion);
                instrument.setDetail(detalle);
                instrument.setDeleted(false);
                instrument.setAvailable(true);
                instrumentRepository.save(instrument);

                List<String> imagenes = (List<String>) instrumentoData.get("imagen");
                List<Image> imageList = new ArrayList<>();
                for (String imagenUrl : imagenes) {
                    Image image = new Image();
                    image.setImage(imagenUrl);
                    image.setDeleted(false);
                    imageRepository.save(image);
                    imageList.add(image);
                }
                instrument.setImage(imageList);
                instrumentRepository.save(instrument);
                instruments.add(instrument);
            }
        }

        return instruments;
    }

}
