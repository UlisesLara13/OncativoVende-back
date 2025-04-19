package com.example.OncativoVende.dtos.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    private BigDecimal rating;

}
