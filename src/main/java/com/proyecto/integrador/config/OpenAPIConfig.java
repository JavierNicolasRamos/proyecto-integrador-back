package com.proyecto.integrador.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${project.openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("equpo1@gmail.com");
        contact.setName("equipo1");
        contact.setUrl("https://www.equipo1.com.ar");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("API MusicRental-BackEnd")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage proyecto-integrador-back.").termsOfService("https://www.equipo1.com.ar/terms")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
