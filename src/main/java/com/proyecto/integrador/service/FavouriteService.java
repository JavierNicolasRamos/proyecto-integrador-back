package com.proyecto.integrador.service;

import com.proyecto.integrador.commons.UserValidation;
import com.proyecto.integrador.dto.FavouriteDto;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.exception.InstrumentAddFavouriteException;
import com.proyecto.integrador.exception.InstrumentGetFavouriteException;
import com.proyecto.integrador.exception.InstrumentRemoveFavouriteException;
import com.proyecto.integrador.exception.UserValidationException;
import com.proyecto.integrador.repository.InstrumentRepository;
import com.proyecto.integrador.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class FavouriteService {

    private static final Logger logger = Logger.getLogger(FavouriteService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private UserValidation userValidation;

    @Transactional
    public List<Instrument>  addFavourite(FavouriteDto favouriteDto){
        logger.info("Iniciando agregado de favorito");
        try{
            User user = this.userRepository.findByEmail(favouriteDto.getEmail());
            Optional<Instrument> instrument = instrumentRepository.findById(favouriteDto.getIdInstrument());

            this.userValidation.userValidation(user.getEmail(), instrument.get().getSeller().getEmail());

            List<Instrument> favourites = user.getFavourites();
            favourites.add(instrument.get());
            user.setFavourites(favourites);
            userRepository.save(user);
            return favourites;
        }
        catch (UserValidationException e ){
            throw new UserValidationException("No se puede agregar a favoritos un instrumento que usted creo");
        }
        catch (Exception e){
            throw new InstrumentAddFavouriteException("Error al intentar agregar el instrumento con ID: " + favouriteDto.getIdInstrument() + "" +
                    "de la lista de favoritos del usuario con email: "  + favouriteDto.getEmail());
        }
    }

    public List<Instrument>  removeFavourite(FavouriteDto favouriteDto) {
        logger.info("removiendo de favorito");
        try{
            User user = this.userRepository.findByEmail(favouriteDto.getEmail());
            Optional<Instrument> instrument = instrumentRepository.findById(favouriteDto.getIdInstrument());

            List<Instrument> favourites = user.getFavourites();
            favourites.remove(instrument.get());
            user.setFavourites(favourites);
            userRepository.save(user);
            return favourites;
        }catch (Exception e){
            throw new InstrumentRemoveFavouriteException("Error al intentar remover el instrumento con ID: " + favouriteDto.getIdInstrument() + "" +
                    "de la lista de favoritos del usuario con email: "  + favouriteDto.getEmail());
        }
    }

    public List<Instrument> getFavourites(String email){
        try{
            User user = this.userRepository.findByEmail(email);
            return user.getFavourites();
        }catch (Exception e){
            throw new InstrumentGetFavouriteException ("Error al recuperar la lista de favoritos del usuario");
        }
    }
}
