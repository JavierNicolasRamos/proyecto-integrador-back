package com.proyecto.integrador.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] SWAGGER_PATHS = {"/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs.yaml","/swagger-ui/**", "/webjars/swagger-ui/**"};
    private final JwtFilter jwtFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5174"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(
                        (sessionManagement) -> sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(SWAGGER_PATHS).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.PUT, "/users/*")).hasAnyRole("Super-Admin", "Admin")
                        .requestMatchers(antMatcher("/users/register")).permitAll()
                        .requestMatchers(antMatcher("/auth/**")).permitAll()
                        .requestMatchers(antMatcher("/category/**")).permitAll()
                        .requestMatchers(antMatcher("/instruments/**")).permitAll()
                        .requestMatchers(antMatcher("/booking/**")).permitAll()
                        .requestMatchers(antMatcher("/reviews/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/**")).hasAnyRole("Super-Admin", "Admin", "User")
                        .requestMatchers(antMatcher(HttpMethod.POST, "/**")).hasAnyRole("Super-Admin", "Admin")
                        .requestMatchers(antMatcher(HttpMethod.PUT, "/**")).hasAnyRole("Super-Admin", "Admin")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, "/**")).hasAnyRole("Super-Admin", "Admin")
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
