package com.example.OncativoVende.dtos.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReportDto {

    @NotNull(message = "Reported user id cannot be null")
    private Integer reported_by_user_id;

    @NotNull(message = "Publication id cannot be null")
    private Integer publication_id;

    @NotNull(message = "Reason cannot be null")
    @Size(min = 1, max = 255, message = "Reason must be between 1 and 255 characters")
    private String reason;

}
