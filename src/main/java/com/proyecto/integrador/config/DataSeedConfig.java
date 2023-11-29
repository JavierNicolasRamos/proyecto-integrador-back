package com.proyecto.integrador.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.dto.BuyerDto;
import com.proyecto.integrador.dto.FavouriteDto;
import com.proyecto.integrador.dto.ReviewDto;
import com.proyecto.integrador.entity.*;
import com.proyecto.integrador.enums.Role;
import com.proyecto.integrador.repository.*;
import com.proyecto.integrador.service.FavouriteService;
import com.proyecto.integrador.service.ReviewService;
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

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CharacteristicRepository characteristicRepository;

    @Autowired
    private ReviewRespository reviewRespository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FavouriteService favouriteService;

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

                List<Characteristic> characteristicsList = new ArrayList<>();

                List<Map<String, Object>> characteristicsDataList = (List<Map<String, Object>>) instrumentData.get("characteristics");

                if (characteristicsDataList != null) {
                    for (Map<String, Object> characteristicData : characteristicsDataList) {
                        String characteristicName = (String) characteristicData.get("name");
                        String characteristicIcon = (String) characteristicData.get("icon");

                        Optional<Characteristic> characteristicExist = characteristicRepository.findByName(characteristicName);
                        if (characteristicExist.isEmpty()) {
                            Characteristic characteristic = new Characteristic();
                            characteristic.setName(characteristicName);
                            characteristic.setIcon(characteristicIcon);
                            characteristic.setDeleted(false);
                            this.characteristicRepository.save(characteristic);
                            characteristicsList.add(characteristic);
                        } else {
                            characteristicsList.add(characteristicExist.get());
                        }
                    }
                }

                Optional<User> userExist = userRepository.findById(1L);
                instrument.setSeller(userExist.get());
                instrument.setName(name);
                instrument.setUploadDate(LocalDate.now());
                instrument.setUpdateDate(LocalDate.now());
                instrument.setScore(score);
                instrument.setDetail(detail);
                instrument.setCharacteristics(characteristicsList);
                instrument.setDeleted(false);;
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

    @Bean
    @Order(5)
    public List<Map<String, Object>> bookingsDataSeed() throws IOException {
        InputStream bookingsInputStream = new ClassPathResource("bookingsDataSeed.json").getInputStream();
        ObjectMapper bookingsObjectMapper = new ObjectMapper();

        return bookingsObjectMapper.readValue(bookingsInputStream, new TypeReference<List<Map<String, Object>>>() {});
    }

    @Bean
    @Order(6)
    @Transactional
    public List<Booking> createBookings(@Qualifier("bookingsDataSeed") @NotNull List<Map<String, Object>> bookingsDataSeed) {
        List<Booking> bookings = new ArrayList<>();

        for (Map<String, Object> bookingData : bookingsDataSeed) {
            Integer id = (Integer) bookingData.get("id");
            Boolean activeBooking = (Boolean) bookingData.get("activeBooking");
            String bookingStart = (String) bookingData.get("bookingStart");
            String bookingEnd = (String) bookingData.get("bookingEnd");

            Map<String, Object> categoryData = (Map<String, Object>) bookingData.get("buyerDto");
            String email = (String) categoryData.get("email");

            Map<String, Object> instrumentDto = (Map<String, Object>) bookingData.get("instrumentDto");
            Integer idInstrument = (Integer) instrumentDto.get("id");

            Optional<Booking> bookingExist = bookingRepository.findById(Long.valueOf(id));

            if (bookingExist.isEmpty()) {
                Optional<Instrument> instrumentExist = instrumentRepository.findById(Long.valueOf(idInstrument));

                Booking booking = new Booking();

                booking.setUser(this.userRepository.findByEmail(email));
                booking.setInstrument(instrumentExist.get());
                booking.setActiveBooking(activeBooking);
                booking.setBookingStart(LocalDate.parse(bookingStart));
                booking.setBookingEnd(LocalDate.parse(bookingEnd));
                booking.setDeleted(false);

                instrumentRepository.save(instrumentExist.get());
                bookingRepository.save(booking);
                bookings.add(booking);
            }
        }
        return bookings;
    }

    @Bean
    @Order(6)
    public List<Map<String, Object>> reviewsDataSeed() throws IOException {
        InputStream bookingsInputStream = new ClassPathResource("reviewsDataSeed.json").getInputStream();
        ObjectMapper bookingsObjectMapper = new ObjectMapper();

        return bookingsObjectMapper.readValue(bookingsInputStream, new TypeReference<List<Map<String, Object>>>() {});
    }

    @Bean
    @Order(7)
    @Transactional
    public List<Review> createReviews(@Qualifier("reviewsDataSeed") @NotNull List<Map<String, Object>> reviewsDataSeed) {

        List<Review> reviews = new ArrayList<>();

        for (Map<String, Object> reviewData : reviewsDataSeed) {
            Double score = (Double) reviewData.get("score");
            String instrumentId = (String) reviewData.get("instrumentId");
            String reviewName = (String) reviewData.get("reviewName");
            String reviewDescription = (String) reviewData.get("reviewDescription");

            Map<String, Object> buyerData = (Map<String, Object>) reviewData.get("buyerDto");
            String email = (String) buyerData.get("email");

            Optional<Review> bookingExist = this.reviewRespository.findByBoyerEmailAndInstrumentId(email, Long.valueOf(instrumentId));

            if (bookingExist.isEmpty()) {
                BuyerDto buyerDto = new BuyerDto();
                buyerDto.setEmail(email);

                ReviewDto reviewDto = new ReviewDto();
                reviewDto.setScore(score);
                reviewDto.setReviewName(reviewName);
                reviewDto.setReviewDescription(reviewDescription);
                reviewDto.setInstrumentId(Long.valueOf(instrumentId));
                reviewDto.setBuyerDto(buyerDto);

                Review review = this.reviewService.createReview(reviewDto);
                reviews.add(review);
            }
        }
        return reviews;
    }

    @Bean
    @Order(8)
    public List<Map<String, Object>> favouritesDataSeed() throws IOException {
        InputStream bookingsInputStream = new ClassPathResource("favouritesDataSeed.json").getInputStream();
        ObjectMapper bookingsObjectMapper = new ObjectMapper();

        return bookingsObjectMapper.readValue(bookingsInputStream, new TypeReference<List<Map<String, Object>>>() {});
    }

    @Bean
    @Order(9)
    @Transactional
    public List<List<Instrument>> createFavourites(@Qualifier("favouritesDataSeed") @NotNull List<Map<String, Object>> favouritesDataSeed) {

        List<List<Instrument>> favourites = new ArrayList<>();

        for (Map<String, Object> favouritesData : favouritesDataSeed) {
            Integer idInstrument = (Integer) favouritesData.get("idInstrument");
            String email = (String) favouritesData.get("email");

            Optional<Instrument> instrument = this.instrumentRepository.findById(Long.valueOf(idInstrument));
            User userExist = this.userRepository.findByEmail(email);

            if (!userExist.getFavourites().contains(instrument.get())) {
                FavouriteDto favouriteDto = new FavouriteDto();
                favouriteDto.setEmail(email);
                favouriteDto.setIdInstrument(Long.valueOf(idInstrument));
                List<Instrument> instrumentList = this.favouriteService.addFavourite(favouriteDto);
                favourites.add(instrumentList);
            }
        }
        return favourites;
    }
}
