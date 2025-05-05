package com.example.OncativoVende.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface ValidatorService {

    public boolean isUsernameUnique(String username);

    public boolean isEmailUnique(String email);

}
