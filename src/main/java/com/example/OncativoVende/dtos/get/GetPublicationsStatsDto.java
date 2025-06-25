package com.example.OncativoVende.dtos.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPublicationsStatsDto {

    private Integer totalPublications;

    private Integer activePublications;

    private Integer inactivePublications;

    private Integer totalViews;
}
