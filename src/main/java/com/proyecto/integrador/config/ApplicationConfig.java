package com.proyecto.integrador.config;

import com.proyecto.integrador.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AuthRepository authRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> authRepository.findByEmail(username)
                .map(UserDetails.class::cast)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
