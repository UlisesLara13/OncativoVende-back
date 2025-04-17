package com.example.OncativoVende.dtos.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSubscriptionDto {

    @NotNull(message = "User id cannot be null")
    private Integer user_id;

    @NotNull(message = "Subscription type cannot be null")
    private Integer subscription_type_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate start_date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate end_date;

    @NotNull(message = "Total price cannot be null")
    private BigDecimal total_price;

    private Integer discount_applied;

}
