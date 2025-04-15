package com.example.OncativoVende.dtos.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCategoryDto {
    private Integer id;

    private String description;
}
