package com.proyecto.integrador.commons;

import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.exception.UserValidationException;
import com.proyecto.integrador.service.InstrumentService;
import com.proyecto.integrador.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class userValidation {
    @Autowired
    private UserService userService;

    public void userValidation(String buyerEmail, String sellerEmail, String message){
        if (sellerEmail.equals(buyerEmail)){
            throw new UserValidationException(message);
        }
    }
}
