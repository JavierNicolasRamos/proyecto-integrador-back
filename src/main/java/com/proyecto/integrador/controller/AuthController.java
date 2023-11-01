package com.proyecto.integrador.controller;

import com.proyecto.integrador.request.LoginRequest;
import com.proyecto.integrador.request.RegisterRequest;
import com.proyecto.integrador.response.AutenticationResponse;
import com.proyecto.integrador.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/register")
    public ResposeEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return null;
    }

    @PostMapping("/login")
    public ResposeEntity<AuthenticationResponse> register(
            @RequestBody LoginRequest request
    ) {
        return null;
    }
}
