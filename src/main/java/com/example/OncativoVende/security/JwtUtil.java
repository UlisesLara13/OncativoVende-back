package com.example.OncativoVende.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    public static final long EXPIRATION_TIME = 86_400_000;

    private static final String SECRET_KEY = "GZ0Vj+wF6h3Q5p9c6P2pD72X8F2Fj6WjB6Y6g2Nh7D8=";

    public static String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String usernameToken = extractUsername(token);
        final String usernameUser = userDetails.getUsername();

        return (usernameToken.equals(usernameUser) && !isTokenExpired(token));

    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = validateToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new IllegalArgumentException("Token no válido o manipulado.", e);
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }


}
