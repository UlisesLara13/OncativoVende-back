package com.example.OncativoVende.dtos.post;
import lombok.Data;

@Data
public class PublicationFilterDto {
    private String searchTerm;
    private String category;
    private String location;
    private Double minPrice;
    private Double maxPrice;
    private String tag;
    private String sortBy = "createdAt";    // Campo de ordenamiento (por defecto fecha de creación)
    private String sortDir = "desc";        // Dirección de ordenamiento
    private int page = 0;                  // Página (0 = primera página)
    private int size = 12;                 // Cantidad de elementos por página
}
