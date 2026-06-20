package com.hydrosmart.backend.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
public class CorsConfig {

    // En local toma localhost:4200 por defecto.
    // En Render, sobreescribe la variable de entorno FRONTEND_URL
    // con la URL real del frontend desplegado.
    @Value("${app.cors.allowed-origin:http://localhost:4200}")
    private String allowedOrigin;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Acepta tanto localhost (desarrollo) como la URL de producción
        config.setAllowedOrigins(List.of("http://localhost:4200", allowedOrigin));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}