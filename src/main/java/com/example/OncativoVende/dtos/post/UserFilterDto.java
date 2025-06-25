package com.example.OncativoVende.dtos.post;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserFilterDto {
    private String searchTerm;
    private Boolean active;
    private Boolean verified;
    private List<String> roles;
    private String location;

    private String sortBy = "surname";
    private String sortDir = "asc";
    private int page = 0;
    private int size = 10;
}