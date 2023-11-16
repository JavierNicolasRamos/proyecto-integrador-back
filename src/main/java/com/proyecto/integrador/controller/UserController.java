package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.UserDto;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @NotNull UserDto user) {
        return ResponseEntity.ok(userService.login(user.getEmail(), user.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @NotNull UserDto user) throws Exception {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userService.register(user);
        return ResponseEntity.status(HttpStatus.OK).body("User with email: " + user.getEmail() + " created successfully.");
    }
}
