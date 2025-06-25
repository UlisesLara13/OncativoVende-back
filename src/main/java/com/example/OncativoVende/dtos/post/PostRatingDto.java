package com.example.OncativoVende.dtos.post;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRatingDto {

    @NotNull (message = "Rater user ID cannot be null")
    private Integer rater_user_id;

    @NotNull (message = "Rated user ID cannot be null")
    private Integer rated_user_id;

    @NotNull (message = "Rating cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be at most 5")
    private BigDecimal rating;

    @NotNull (message = "Comment cannot be null")
    @Size(max = 255, message = "Comment must be at most 255 characters")
    private String comment;

}
