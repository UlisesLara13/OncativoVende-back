package com.example.OncativoVende.dtos.post;

import lombok.Data;

@Data
public class ReportFilterDto {
    private String searchTerm;
    private String status;

    private String sortBy = "created_at";
    private String sortDir = "desc";
    private int page = 0;
    private int size = 10;
}