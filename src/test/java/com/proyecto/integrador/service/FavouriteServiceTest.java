package com.proyecto.integrador.service;

import com.proyecto.integrador.commons.UserValidation;
import com.proyecto.integrador.dto.FavouriteDto;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.User;

import com.proyecto.integrador.repository.InstrumentRepository;
import com.proyecto.integrador.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class FavouriteServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InstrumentRepository instrumentRepository;

    @Mock
    private UserValidation userValidation;

    @InjectMocks
    private FavouriteService favouriteService;

    @Test
    void addFavourite() throws Exception{

        FavouriteDto favouriteDto = new FavouriteDto();
        favouriteDto.setEmail("test@example.com");
        favouriteDto.setIdInstrument(1L);

        User user = new User();
        user.setEmail("test@example.com");
        user.setFavourites(new ArrayList<>());

        User seller = new User();
        seller.setEmail("seller@example.com");

        Instrument instrument = new Instrument();
        instrument.setSeller(seller);


        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(instrumentRepository.findById(anyLong())).thenReturn(Optional.of(instrument));


        List<Instrument> favourites = favouriteService.addFavourite(favouriteDto);


        assertEquals(1, favourites.size());
        assertEquals(instrument, favourites.get(0));


        verify(userRepository, times(1)).findByEmail(anyString());
        verify(instrumentRepository, times(1)).findById(anyLong());
        verify(userValidation, times(1)).userValidation(anyString(), anyString());
    }

    @Test
    void removeFavourite() {

        String userEmail = "user@example.com";
        Long instrumentId = 1L;


        User user = new User();
        user.setEmail(userEmail);

        Instrument instrument = new Instrument();
        instrument.setId(instrumentId);

        List<Instrument> favourites = new ArrayList<>();
        favourites.add(instrument);
        user.setFavourites(favourites);


        FavouriteDto favouriteDto = new FavouriteDto();
        favouriteDto.setEmail(userEmail);
        favouriteDto.setIdInstrument(instrumentId);


        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(instrumentRepository.findById(instrumentId)).thenReturn(Optional.of(instrument));


        List<Instrument> result = favouriteService.removeFavourite(favouriteDto);


        assertEquals(0, result.size());
    }

    @Test
    void getFavourites() {
        String userEmail = "user@example.com";


        User user = new User();
        user.setEmail(userEmail);

        Instrument instrument1 = new Instrument();
        instrument1.setId(1L);

        Instrument instrument2 = new Instrument();
        instrument2.setId(2L);

        List<Instrument> favourites = new ArrayList<>();
        favourites.add(instrument1);
        favourites.add(instrument2);
        user.setFavourites(favourites);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);


        List<Instrument> result = favouriteService.getFavourites(userEmail);


        assertEquals(favourites, result);
    }
}