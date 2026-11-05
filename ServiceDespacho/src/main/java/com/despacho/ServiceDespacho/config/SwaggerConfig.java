package com.despacho.ServiceDespacho.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI despachoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Microservicio Despacho")
                        .version("1.0")
                        .description("Documentación de endpoints del microservicio de despacho de FullComponents Store"));
    }
}
