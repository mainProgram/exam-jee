package com.groupeisi.services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION,
                "Access-Control-Request-Headers"
        ));
        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"
        ));
        config.setExposedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}