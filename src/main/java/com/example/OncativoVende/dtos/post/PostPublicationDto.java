package com.example.OncativoVende.dtos.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class PostPublicationDto {

    @NotNull(message = "User id cannot be null")
    private Integer user_id;

    @NotNull(message = "User id cannot be null")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    private BigDecimal price;

    @NotNull(message = "Location cannot be null")
    private Integer location_id;

    @NotNull(message = "Categories cannot be null")
    private List<Integer> categories;

    @NotNull(message = "Tags cannot be null")
    private List<Integer> tags;

    private List<String> images;

    private List<PostContact> contacts;

    private String coords;

}
