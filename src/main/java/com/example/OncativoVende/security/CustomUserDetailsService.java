package com.example.OncativoVende.security;

import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.services.UserService;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Data
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            GetUserDto user = userService.getUserByEmail(username);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            return new CustomUserDetails(user);
        } catch (UsernameNotFoundException ex) {
            throw new UsernameNotFoundException("User not found", ex);
        }
    }

}
