package com.example.OncativoVende.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLoginDto {

    private String email;

    private String password;

}
