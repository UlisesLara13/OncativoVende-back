package com.example.OncativoVende.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Definir las rutas a las carpetas de las imágenes de perfil y publicación
    private static final String PROFILE_PIC_DIRECTORY = "C:/Users/USER/Desktop/tesis/OncativoVende-front/Oncativo-Vende/src/assets/Profiles/";
    private static final String PUBLICATION_PIC_DIRECTORY = "C:/Users/USER/Desktop/tesis/OncativoVende-front/Oncativo-Vende/src/assets/Publications/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Hacer que las imágenes de perfil sean accesibles a través de "/assets/profile-pics/"
        registry.addResourceHandler("/assets/profile-pics/**")
                .addResourceLocations("file:" + PROFILE_PIC_DIRECTORY);

        // Hacer que las imágenes de publicaciones sean accesibles a través de "/assets/publication-pics/"
        registry.addResourceHandler("/assets/publication-pics/**")
                .addResourceLocations("file:" + PUBLICATION_PIC_DIRECTORY);
    }
}

