package com.auer.voce_fit.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:http://localhost:4200,http://localhost:5173}")
    private String[] allowedOrigins;

    // Métodos HTTP permitidos
    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String[] allowedMethods;

    // Cabeçalhos permitidos (por padrão: todos)
    @Value("${cors.allowed-headers:*}")
    private String[] allowedHeaders;

    // Se permite envio de cookies/autenticação cruzada
    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    // Tempo máximo (em segundos) para o navegador manter o CORS em cache
    @Value("${cors.max-age:3600}")
    private long maxAge;

    /**
     * Configuração CORS global com `WebMvcConfigurer`.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders)
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }


}
