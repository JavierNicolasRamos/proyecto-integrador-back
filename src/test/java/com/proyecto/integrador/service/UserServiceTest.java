package com.proyecto.integrador.service;

import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByEmail() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllAdminUsers() {
    }

    @Test
    void findAllNormalUsers() {
    }

    @Test
    void deleteUserById() {
    }

    @Test
    void updateUserById() {
    }

    @Test
    void createUser() {
    }

    @Test
    void login() {
    }

    @Test
    void register() {
    }
}