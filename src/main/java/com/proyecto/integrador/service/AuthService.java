package com.proyecto.integrador.service;

import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Iniciando el proceso de autenticación de usuario...");
        try {
            User user = userRepository.findByEmail(email);
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getUserRole().getDescription())
                    .disabled(!user.getIsActive())
                    .build();
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("Las credenciales son incorrectas");
        } catch (Exception e) {
            throw new UsernameNotFoundException("User " + email + " not found.");
        }
    }
}