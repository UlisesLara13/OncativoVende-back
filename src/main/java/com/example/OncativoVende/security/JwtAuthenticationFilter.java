package com.example.OncativoVende.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Data
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtService;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                    jakarta.servlet.http.HttpServletResponse response,
                                    jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {

        // Obtener token de la cabecera
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        // Validar formato de token y extraer JWT
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);
        }

        // Validar usuario y token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = customUserDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(HttpServletRequest.class.cast(request)));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
