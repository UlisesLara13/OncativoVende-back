package com.example.OncativoVende.dtos.mp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private String title;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
}
