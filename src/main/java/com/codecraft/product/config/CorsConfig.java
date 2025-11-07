package com.codecraft.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración básica de CORS para permitir solicitudes desde cualquier origen.
 */
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Configuras el endpoint al que se aplicará CORS
                        .allowedOrigins("*")  // Permites solicitudes desde cualquier origen
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // Métodos HTTP permitidos
                        .allowedHeaders("*") // Permites todos los encabezados
                        .allowCredentials(false); // No permites el envío de credenciales
            }
        };
    }
}

