package com.example.OncativoVende.dtos.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSolveReportDto {

    @NotNull(message = "Report ID cannot be null")
    private Integer reportId;

    @NotNull(message = "Reason cannot be null")
    @Size(min = 1, max = 255, message = "Response must be between 1 and 255 characters")
    private String response;

}
