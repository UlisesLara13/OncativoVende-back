package com.example.OncativoVende.dtos.dashboards;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicationDashboardDto {

    private Long totalPublications;

    private Long activePublications;
    private Long inactivePublications;

    private Long totalViews;

    private BigDecimal averagePrice;

    private String yearAnalize;

    private List<LabelCountDto> publicationsByCategory;
    private List<LabelCountDto> publicationsByTag;
    private List<LabelCountDto> publicationsByLocation;
}
