package com.example.OncativoVende.dtos.mp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private String preferenceId;
    private String initPoint;
    private String sandboxInitPoint;
}
