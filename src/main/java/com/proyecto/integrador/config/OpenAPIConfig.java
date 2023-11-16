package com.proyecto.integrador.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info =@Info(
                title = "API MusicRental-BackEnd",
                version = "1.0",
                contact = @Contact(
                        name = "Equipo 1", email = "equpo1@gmail.com"
                ),
                license = @License(
                        name = "MIT License", url = "https://choosealicense.com/licenses/mit/"
                ),
                termsOfService = "https://www.equipo1.com.ar/terms",
                description = "This API exposes endpoints to manage MusicRental-BackEnd."
        ),
        servers = @Server(
                url = "${project.openapi.dev-url}",
                description = "Server URL in Development environment"
        )
)
public class OpenAPIConfig {
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
