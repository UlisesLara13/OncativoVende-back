package com.example.OncativoVende.dtos.mp;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private String description;

    private String payerEmail;
    private List<ItemDTO> items;
}
