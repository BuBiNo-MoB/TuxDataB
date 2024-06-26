package com.database.TuxDataB.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200"); // Origine consentita
        configuration.addAllowedMethod("*"); // Consenti tutti i metodi
        configuration.addAllowedHeader("*"); // Consenti tutte le intestazioni
        configuration.setAllowCredentials(true);

        // Setta i permessi per le richieste preflight (OPTIONS)
        configuration.addExposedHeader("Authorization");
        configuration.setMaxAge(3600L); // Cache delle preflight per 1 ora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }
}
