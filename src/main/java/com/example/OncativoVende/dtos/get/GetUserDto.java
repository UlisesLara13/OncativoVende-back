package com.example.OncativoVende.dtos.get;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class GetUserDto {

    private Integer id;

    private String name;

    private String surname;

    private String username;

    private String email;

    private String password;

    private Boolean active;

    private Boolean verified;

    private String location;

    private String avatar_url;

    private String[] roles;

    private BigDecimal rating;

    private String subscription;

}
