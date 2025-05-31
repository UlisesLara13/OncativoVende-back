package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.dtos.post.ChangePassword;
import com.example.OncativoVende.dtos.post.PostLoginDto;
import com.example.OncativoVende.dtos.post.RecoveryRequestDto;
import com.example.OncativoVende.dtos.post.ResetPasswordDto;
import com.example.OncativoVende.entities.UserEntity;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.security.JwtUtil;
import com.example.OncativoVende.security.PasswordUtil;
import com.example.OncativoVende.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody PostLoginDto credentials) {
        GetUserDto user = userService.verifyLogin(credentials);

        if (user != null) {
            // Crear el mapa de claims con todos los campos de GetUserDto
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("name", user.getName());
            claims.put("surname", user.getSurname());
            claims.put("username", user.getUsername());
            claims.put("email", user.getEmail());
            claims.put("active", user.getActive());
            claims.put("avatar_url", user.getAvatar_url());
            claims.put("roles", user.getRoles());
            claims.put("rating", user.getRating());
            claims.put("location", user.getLocation());
            claims.put("verified", user.getVerified());


            String token = "";
            // Generar el token
            if (user.getEmail() == null) {
                token = JwtUtil.generateToken(user.getUsername(), claims);
            }
            else {
                token = JwtUtil.generateToken(user.getEmail(), claims);
            }

            // Devolver el token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        } else {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    @PostMapping("/validateToken")
    public Map<String, String> validateToken(@RequestBody String token) {
        Map<String, String> response = new HashMap<>();
        try {
            //JwtUtil.validateToken(token);
            response.put("message", "Valid token");
        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid token");
            System.out.println(e.getMessage());
        }
        return response;
    }

    @PutMapping("/changePassword")
    public Map<String, String> changePassword(@RequestBody ChangePassword changePasswordDto) {
        userService.changePassword(changePasswordDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Contraseña actualizada exitosamente");
        return response;
    }

    @PostMapping("/recover-password")
    public ResponseEntity<?> sendRecoveryCode(@RequestBody RecoveryRequestDto request) {
        userService.sendRecoveryCode(request);
        return ResponseEntity.ok(Map.of("message", "Código enviado al correo."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto dto) {
        userService.resetPassword(dto);
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente."));
    }

}
