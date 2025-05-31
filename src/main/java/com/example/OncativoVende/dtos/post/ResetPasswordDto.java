package com.example.OncativoVende.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
    private String emailOrUsername;
    private String code;
    private String newPassword;
}
