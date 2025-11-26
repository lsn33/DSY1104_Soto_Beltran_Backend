package com.mitienda.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {

        // Declaramos el esquema de seguridad JWT
        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .description("Introduce tu token JWT con el formato: Bearer <token>")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // Lo agregamos como requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("API MiTienda")
                        .description("Documentación de la API del proyecto MiTienda")
                        .version("v1.0")
                        .contact(new Contact().name("Equipo MiTienda").email("equipo@mitienda.com"))
                )
                .schemaRequirement("bearerAuth", securityScheme)
                .addSecurityItem(securityRequirement);
    }
}
