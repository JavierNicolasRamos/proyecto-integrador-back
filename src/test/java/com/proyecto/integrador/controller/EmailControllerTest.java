package com.proyecto.integrador.controller;
import com.proyecto.integrador.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class EmailControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void resendRegisterEmail_Success() {
        String email = "test@example.com";
        doNothing().when(userService).resendRegisterEmail(email);

        ResponseEntity<String> response = emailController.resendRegisterEmail(email);

        assertEquals(new ResponseEntity<>("Email reenviado con éxito", HttpStatus.OK), response);
    }

    @Test
    public void resendRegisterEmail_Failure() {
        String email = "test@example.com";
        doThrow(new RuntimeException("Error message")).when(userService).resendRegisterEmail(email);

        ResponseEntity<String> response = emailController.resendRegisterEmail(email);

        assertEquals(new ResponseEntity<>("Error message", HttpStatus.BAD_REQUEST), response);
    }
}