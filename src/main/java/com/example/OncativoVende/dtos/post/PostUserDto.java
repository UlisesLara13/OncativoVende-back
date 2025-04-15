package com.example.OncativoVende.dtos.post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostUserDto {

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 15, message = "Name must be between 2 and 15 characters")
    private String name;

    @NotNull(message = "Surname cannot be null")
    @Size(min = 2, max = 15, message = "Surname must be between 2 and 15 characters")
    private String surname;

    @NotNull(message = "Username cannot be null")
    @Size(min= 5 , message = "Username must be at least 5 characters")
    private String username;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email must be in a valid format")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private Integer location_id;

    private String avatar_url;

    @NotNull(message = "Role cannot be null")
    private Integer[] roles;

}
