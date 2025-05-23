package com.example.OncativoVende.dtos.post;
import lombok.Data;

import java.util.List;

@Data
public class PublicationFilterDto {
    private String searchTerm;
    List<String> categories;
    List<String> tags;
    private String location;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy = "createdAt";    // Campo de ordenamiento (por defecto fecha de creación)
    private String sortDir = "desc";        // Dirección de ordenamiento
    private int page = 0;                  // Página (0 = primera página)
    private int size = 12;                 // Cantidad de elementos por página
}
