package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.services.ValidatorService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@RequiredArgsConstructor
@Service
public class ValidatorServiceImpl implements ValidatorService {

    private final UserRepository userRepository;

    @Override
    public boolean isUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }
}
