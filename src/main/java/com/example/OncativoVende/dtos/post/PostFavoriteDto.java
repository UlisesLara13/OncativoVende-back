package com.example.OncativoVende.dtos.post;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostFavoriteDto {

    @NotNull(message = "Publication id cannot be null")
    private Integer publication_id;

    @NotNull(message = "User id cannot be null")
    private Integer user_id;
}
