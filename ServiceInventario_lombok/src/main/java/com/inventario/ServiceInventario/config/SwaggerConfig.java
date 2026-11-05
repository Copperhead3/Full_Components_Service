package com.inventario.ServiceInventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI inventarioOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API Inventario - FullComponents")
                .version("1.0")
                .description("Documentación del microservicio de inventario, reservas, movimientos y alertas de stock."));
    }
}
