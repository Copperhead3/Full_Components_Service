package com.carrito.ServiceCarrito.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI carritoOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API ServiceCarrito")
                .version("1.0")
                .description("Documentación del microservicio de carrito de FullComponents Store"));
    }
}
