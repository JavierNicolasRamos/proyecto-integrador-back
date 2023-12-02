package com.proyecto.integrador.controller;

import com.proyecto.integrador.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private UserService userService;

    @GetMapping("/resend/{email}")
    public ResponseEntity<String> resendRegisterEmail(@PathVariable("email") String email) {
        try {
            userService.resendRegisterEmail(email);
            return new ResponseEntity<>("Email reenviado con éxito", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
