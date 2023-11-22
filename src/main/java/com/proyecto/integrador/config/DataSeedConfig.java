package com.proyecto.integrador.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Image;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.enums.Role;
import com.proyecto.integrador.repository.CategoryRepository;
import com.proyecto.integrador.repository.ImageRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
import com.proyecto.integrador.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

@Configuration
public class DataSeedConfig {
    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    @Order(1)
    public List<Map<String, Object>> usersDataSeed() throws IOException {
        InputStream usersInputStream = new ClassPathResource("usersDataSeed.json").getInputStream();
        ObjectMapper usersObjectMapper = new ObjectMapper();

        return usersObjectMapper.readValue(usersInputStream, new TypeReference<List<Map<String, Object>>>() {});
    }

    @Bean
    @Order(2)
    public List<User> createUsers(@Qualifier("usersDataSeed") @NotNull List<Map<String, Object>> usersDataSeed) {
        List<User> users = new ArrayList<>();

        for (Map<String, Object> userData : usersDataSeed) {
            String name = (String) userData.get("name");
            String surname = (String) userData.get("surname");
            String email = (String) userData.get("email");
            String password = (String) userData.get("password");
            Integer areaCode = (Integer) userData.get("areaCode");
            Integer prefix = (Integer) userData.get("prefix");
            Integer phone = (Integer) userData.get("phone");
            Boolean isMobile = (Boolean) userData.get("isMobile");

            String roleString = (String) userData.get("role");
            Role role = Role.valueOf(roleString.toUpperCase());

            Optional<User> userExist = Optional.ofNullable(userRepository.findByEmail(email));
            if (userExist.isEmpty()) {
                User user = new User();
                user.setName(name);
                user.setSurname(surname);
                user.setEmail(email);
                String encryptedPassword = this.passwordEncoder.encode(password);
                user.setPassword(encryptedPassword);
                user.setAreaCode(areaCode);
                user.setPrefix(prefix);
                user.setPhone(phone);
                user.setIsMobile(isMobile);
                user.setUserRole(role);
                user.setIsActive(true);
                user.setDeleted(false);
                userRepository.save(user);
                users.add(user);
            }
        }
        return users;
    }

    @Bean
    @Order(3)
    public List<Map<String, Object>> instrumentsDataSeed() throws IOException {
        InputStream instrumentsInputStream = new ClassPathResource("instrumentsDataSeed.json").getInputStream();
        ObjectMapper instrumentsObjectMapper = new ObjectMapper();

        return instrumentsObjectMapper.readValue(instrumentsInputStream, new TypeReference<List<Map<String, Object>>>() {});
    }

    @Bean
    @Order(4)
    @Transactional
    public List<Instrument> createInstruments(@Qualifier("instrumentsDataSeed") @NotNull List<Map<String, Object>> instrumentsDataSeed) {
        List<Instrument> instruments = new ArrayList<>();

        for (Map<String, Object> instrumentData : instrumentsDataSeed) {
            String name = (String) instrumentData.get("name");
            Double score = (Double) instrumentData.get("score");
            String detail = (String) instrumentData.get("detail");

            Map<String, Object> categoryData = (Map<String, Object>) instrumentData.get("category");
            String categoryName = (String) categoryData.get("name");
            String categoryDetails = (String) categoryData.get("details");
            String categoryUrl = (String) categoryData.get("url");

            Optional<Instrument> instrumentExist = instrumentRepository.getByName(name);
            if (instrumentExist.isEmpty()) {
                Instrument instrument = new Instrument();
                Optional<Category> CategoryExist = categoryRepository.findByName(categoryName);

                if (CategoryExist.isEmpty()) {
                    Category category = new Category();
                    category.setName(categoryName);
                    category.setDetails(categoryDetails);
                    category.setDeleted(false);

                    Image image = new Image();
                    image.setImage(categoryUrl);
                    image.setDeleted(false);
                    this.imageRepository.save(image);
                    category.setImage(image);

                    this.categoryRepository.save(category);
                    instrument.setCategory(category);
                } else {
                    instrument.setCategory(CategoryExist.get());
                }

                Optional<User> userExist = userRepository.findById(1L);
                instrument.setSeller(userExist.get());
                instrument.setName(name);
                instrument.setUploadDate(LocalDate.now());
                instrument.setUpdateDate(LocalDate.now());
                instrument.setScore(score);
                instrument.setDetail(detail);
                instrument.setDeleted(false);
                instrument.setAvailable(true);
                instrument.setReviewCount(0L);
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
