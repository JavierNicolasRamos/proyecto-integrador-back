package com.proyecto.integrador.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.config.JwtUtil;
import com.proyecto.integrador.dto.UserDto;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @MockBean
    private JwtUtil jwtUtil;


    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(username = "test", roles = {"User"})
    void getUserByEmail() throws Exception {
        String email = "test@example.com";

        mockMvc.perform(get("/users/email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void getUserById()  throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/users/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void getAll() throws Exception{
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Nombre1");
        user1.setEmail("correo1@ejemplo.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Nombre2");
        user2.setEmail("correo2@ejemplo.com");

        List<User> userList = Arrays.asList(user1, user2);


        Mockito.when(userService.findAll())
                .thenReturn(userList);


        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].name").value(user1.getName()))
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$[1].name").value(user2.getName()))
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()));


        Mockito.verify(userService, Mockito.times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void getAllAdminUsers() {

        List<User> mockAdminUsers = new ArrayList<>();
        mockAdminUsers.add(new User());
        mockAdminUsers.add(new User());
        when(userService.findAllAdminUsers()).thenReturn(mockAdminUsers);


        List<User> result = userService.findAllAdminUsers();


        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void getAllNormalUsers() {

        List<User> mockNormalUsers = new ArrayList<>();
        mockNormalUsers.add(new User());
        mockNormalUsers.add(new User());
        when(userService.findAllNormalUsers()).thenReturn(mockNormalUsers);


        List<User> result = userService.findAllNormalUsers();


        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @WithMockUser(username = "test", roles = {"User"})
    void updateUser() throws Exception{
    try {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setName("NuevoNombre");
        userDto.setEmail("nuevo@correo.com");


        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName(userDto.getName());
        updatedUser.setEmail(userDto.getEmail());


        Mockito.when(userService.updateUserById(Mockito.eq(userId), Mockito.any(UserDto.class)))
                .thenReturn(updatedUser);


        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDto.getEmail()));


        Mockito.verify(userService, Mockito.times(1))
                .updateUserById(Mockito.eq(userId), Mockito.eq(userDto));
        }catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Test
    @WithMockUser(username = "test", roles = {"User"})
    void deleteUser() throws Exception{

        Long userId = 1L;


        User deletedUser = new User();
        deletedUser.setId(userId);
        deletedUser.setName("Deleted User");
        deletedUser.setEmail("deleted@user.com");


        Mockito.when(userService.deleteUserById(Mockito.eq(userId)))
                .thenReturn(deletedUser);


        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Mockito.verify(userService, Mockito.times(1))
                .deleteUserById(Mockito.eq(userId));
    }


    @Test
    @WithMockUser(username = "test", roles = {"User"})
    void register() throws Exception{

        UserDto userDto = new UserDto();
        userDto.setName("Nombre");
        userDto.setEmail("correo@ejemplo.com");


        Mockito.doNothing().when(userService).register(userDto);


        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto))
                        .with(csrf()))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();


        Mockito.verify(userService, Mockito.times(1)).register(userDto);

    }

    @Test
    @WithMockUser(username = "test", roles = {"User"})
    void testResendRegisterEmail() throws Exception {
        String email = "test@example.com";

        mockMvc.perform(get("/users/resendRegisterEmail/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Email reenviado con éxito"));
    }



}