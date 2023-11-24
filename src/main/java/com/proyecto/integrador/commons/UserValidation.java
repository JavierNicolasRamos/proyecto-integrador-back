package com.proyecto.integrador.commons;

import com.proyecto.integrador.exception.UserValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserValidation {

    public void userValidation(String buyerEmail, String sellerEmail){
        if (sellerEmail.equals(buyerEmail)){
            throw new UserValidationException();
        }
    }
}
