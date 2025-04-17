package com.example.OncativoVende.dtos.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetShortUserDto {
    private Integer id;

    private String name;

    private String surname;

    private Boolean verified;

    private String avatar_url;

}
