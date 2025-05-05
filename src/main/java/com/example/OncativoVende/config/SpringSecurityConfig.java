package com.example.OncativoVende.config;

import com.example.OncativoVende.security.CustomUserDetailsService;
import com.example.OncativoVende.security.JwtAuthenticationFilter;
import com.example.OncativoVende.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtUtil jwtService;

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, customUserDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                    .csrf(csrf -> csrf.disable())
                    .cors(cors -> cors.disable())
                    .authorizeHttpRequests(authorize -> authorize
                                    .requestMatchers("/auth/**", "/v3/api-docs/**",
                                            "/swagger-ui/**",
                                            "/swagger-ui/index.html").permitAll()
                                    .anyRequest().permitAll()
                            //.anyRequest().authenticated()
                            // Lo dejo en permitAll para que no haya problemas con el front,
                            // especialmente para que no lo necesiten otros grupos
                    )
                    .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

            return http.build();
        } catch (Exception e) {
            throw new IllegalStateException("Error al configurar la seguridad", e);
        }
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) {
        try {
            AuthenticationManagerBuilder authenticationManagerBuilder =
                    http.getSharedObject(AuthenticationManagerBuilder.class);
            authenticationManagerBuilder.userDetailsService(customUserDetailsService);
            return authenticationManagerBuilder.build();
        } catch (Exception e) {
            throw new IllegalStateException("Error al configurar el AuthenticationManager", e);
        }
    }

}
