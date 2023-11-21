package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.UserDto;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/email/{email}")
    public User getUserByEmail(String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/id/{id}")
    public User getUserById(Long id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/admins")
    public List<User> getAllAdminUsers() {
        return userService.findAllAdminUsers();
    }

    @GetMapping("/normals")
    public List<User> getAllNormalUsers() {
        return userService.findAllNormalUsers();
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,@RequestBody UserDto user) throws Exception {
        return userService.updateUserById(id, user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @NotNull UserDto user) throws Exception {
        try {
            userService.findByEmail(user.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with email: " + user.getEmail() + " already exists.");
        } catch (Exception e) {
        throw new Exception("Error al registrar el usuario: " + e.getMessage());
        }
    }
}
