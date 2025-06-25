package com.example.OncativoVende.dtos.post;

import lombok.Data;

@Data
public class PublicationByUserFilterDto {
    private String searchTerm;
    private Boolean active;
    private String sortBy = "createdAt";
    private String sortDir = "desc";
    private int page = 0;
    private int size = 10;
}
