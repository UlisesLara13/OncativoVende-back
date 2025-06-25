package com.example.OncativoVende.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MercadoPagoConfiguration implements WebMvcConfigurer {

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken("APP_USR-8915559851236409-052602-83a7c5798b066a2c15b16a5fe4b9d9bf-2461602818");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
