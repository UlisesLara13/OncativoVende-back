package com.example.OncativoVende.controllers;

import com.example.OncativoVende.services.ValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/validator")
@RequiredArgsConstructor
public class ValidatorController {

    private final ValidatorService validatorService;

    @GetMapping("/username")
    public ResponseEntity<Map<String, Boolean>> usernameUnique(@RequestParam String username) {
        boolean isUnique = validatorService.isUsernameUnique(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isUnique", isUnique);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email")
    public ResponseEntity<Map<String, Boolean>> emailUnique(@RequestParam String email) {
        boolean isUnique = validatorService.isEmailUnique(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isUnique", isUnique);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/current")
    public ResponseEntity<Map<String, Boolean>> emailUniqueAndNotCurrentUser(@RequestParam String email, @RequestParam Integer userId) {
        boolean isUnique = validatorService.isEmailUniqueAndNotCurrentUser(email, userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isUnique", isUnique);
        return ResponseEntity.ok(response);
    }
}
