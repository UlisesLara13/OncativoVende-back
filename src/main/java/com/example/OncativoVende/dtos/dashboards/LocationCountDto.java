package com.example.OncativoVende.dtos.dashboards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocationCountDto {
    private String location;
    private Long count;
}
