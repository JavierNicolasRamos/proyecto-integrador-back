package com.proyecto.integrador.controller;

import com.proyecto.integrador.config.JwtUtil;
import com.proyecto.integrador.dto.AuthDto;
import com.proyecto.integrador.enums.Role;
import com.proyecto.integrador.exception.UserNotFoundException;
import com.proyecto.integrador.response.AuthResponse;
import com.proyecto.integrador.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;
    @Autowired
    UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @NotNull AuthDto authDto) throws Exception {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword());

        Authentication authentication = this.authenticationManager.authenticate(login);

        String jwt = this.jwtUtil.create(authDto.getEmail());

        String email = authDto.getEmail();
        Role role = userService.getRoleByEmail(email);

        AuthResponse response = new AuthResponse(jwt, role, email);

        if(jwt != null){
            return ResponseEntity.ok().header("Authorization", jwt).body(response.toString());
        } else {
            throw new UserNotFoundException("User with email: " + authDto.getEmail() + " was not found.");
        }
    }
}
