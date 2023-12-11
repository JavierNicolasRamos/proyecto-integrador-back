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
        configuration.setAllowedOrigins(List.of("http://localhost:4173"));
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
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> response.sendError(401))
                        .accessDeniedHandler((request, response, accessDeniedException) -> response.sendError(403))
                )
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(SWAGGER_PATHS).permitAll()
                        .requestMatchers(antMatcher("/booking/occupied-dates/**")).permitAll()
                        .requestMatchers(antMatcher("/users/register")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/category/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/reviews/**")).permitAll()
                        .requestMatchers(antMatcher("/auth/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/instruments/**")).permitAll()
                        .requestMatchers(antMatcher("/emails/resend/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.PUT, "/users/**")).hasAnyRole("Super-Admin", "Admin")
                        .requestMatchers(antMatcher(HttpMethod.GET, "/users/**")).hasAnyRole("Super-Admin", "Admin","User")
                        .requestMatchers(antMatcher(HttpMethod.DELETE, "/users/**")).hasAnyRole("Super-Admin", "Admin","User")
                        .requestMatchers(antMatcher("/users/register")).permitAll()
                        .requestMatchers(
                                antMatcher(HttpMethod.POST, "/category/**"),
                                antMatcher(HttpMethod.PUT, "/category/**"),
                                antMatcher(HttpMethod.DELETE, "/category/**")).hasAnyRole("Super-Admin", "Admin")
                        .requestMatchers(
                                antMatcher(HttpMethod.POST, "/instruments/**"),
                                antMatcher(HttpMethod.PUT, "/instruments/**"),
                                antMatcher(HttpMethod.DELETE, "/instruments/**")).hasAnyRole("Super-Admin", "Admin")
                        .requestMatchers(
                                antMatcher(HttpMethod.GET, "/booking/**"),
                                antMatcher(HttpMethod.POST, "/booking/**"),
                                antMatcher(HttpMethod.PUT, "/booking/**"),
                                antMatcher(HttpMethod.DELETE, "/booking/**")).hasAnyRole("Super-Admin", "Admin", "User")
                        .requestMatchers(
                                antMatcher(HttpMethod.POST, "/reviews/**"),
                                antMatcher(HttpMethod.PUT, "/reviews/**"),
                                antMatcher(HttpMethod.DELETE, "/reviews/**")).hasAnyRole("Super-Admin", "Admin", "User")
                        .requestMatchers(
                                antMatcher(HttpMethod.GET, "/favourite/**"),
                                antMatcher(HttpMethod.POST, "/favourite/**"),
                                antMatcher(HttpMethod.PUT, "/favourite/**"),
                                antMatcher(HttpMethod.DELETE, "/favourite/**")).hasAnyRole("Super-Admin", "Admin", "User")
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
