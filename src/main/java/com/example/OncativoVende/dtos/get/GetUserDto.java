package com.example.OncativoVende.dtos.get;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

}
