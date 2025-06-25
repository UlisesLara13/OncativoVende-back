package com.example.OncativoVende.dtos.dashboards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelCountDto {
    private String label;
    private Long count;
}
