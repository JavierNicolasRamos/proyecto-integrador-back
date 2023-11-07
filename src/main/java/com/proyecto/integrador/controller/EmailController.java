package com.proyecto.integrador.controller;

import com.proyecto.integrador.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendCustomEmail() {
        // emailRequest es un objeto que contiene el destinatario, asunto y contenido HTML
        this.emailService.sendEmail();

        return ResponseEntity.ok("Correo electrónico enviado con éxito");
    }
}