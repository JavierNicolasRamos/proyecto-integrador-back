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

        return objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
    }

    @Bean
    @Order(2)
    @Transactional
    public List<Instrument> createInstrument(@Qualifier("dataSeed") List<Map<String, Object>> dataSeed) {
        List<Instrument> instruments = new ArrayList<>();

        for (Map<String, Object> instrumentData : dataSeed) {
            String name = (String) instrumentData.get("name");
            Double score = (Double) instrumentData.get("score");
            String detail = (String) instrumentData.get("detail");
            Map<String, Object> categoryData = (Map<String, Object>) instrumentData.get("category");
            String categoryName = (String) categoryData.get("name");
            String categoryDetails = (String) categoryData.get("details");


            Optional<Instrument> instrumentExist = instrumentRepository.getByName(name);
            if (instrumentExist.isEmpty()) {
                Instrument instrument = new Instrument();
                Optional<Category> CategoryExist = categoryRepository.findByName(categoryName);
                if (CategoryExist.isEmpty()) {
                    Category category = new Category();
                    category.setName(categoryName);
                    category.setDetails(categoryDetails);
                    category.setDeleted(false);
                    this.categoryRepository.save(category);
                    instrument.setCategory(category);
                } else {
                    instrument.setCategory(CategoryExist.get());
                }

                instrument.setName(name);
                instrument.setUploadDate(LocalDate.now());
                instrument.setUpdateDate(LocalDate.now());
                instrument.setScore(score);
                instrument.setDetail(detail);
                instrument.setDeleted(false);
                instrument.setAvailable(true);
                instrumentRepository.save(instrument);

                List<String> images = (List<String>) instrumentData.get("image");
                List<Image> imageList = new ArrayList<>();
                for (String imageUrl : images) {
                    Image image = new Image();
                    image.setImage(imageUrl);
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
