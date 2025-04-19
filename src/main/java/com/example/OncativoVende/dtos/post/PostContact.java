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
public class PostContact {

    @NotNull(message = "Contact type id cannot be null")
    private Integer contact_type_id;

    @NotNull(message = "Contact value cannot be null")
    private String contact_value;
}
