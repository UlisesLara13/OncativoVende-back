package com.example.OncativoVende.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface ValidatorService {

    boolean isUsernameUnique(String username);

    boolean isEmailUnique(String email);

    boolean isEmailUniqueAndNotCurrentUser(String email, Integer userId);

    boolean isNotBanned(String email, String username);

}
