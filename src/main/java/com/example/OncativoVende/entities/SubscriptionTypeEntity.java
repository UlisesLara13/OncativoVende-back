package com.example.OncativoVende.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Entity
@Table(name = "subscriptiontypes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SubscriptionTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column
    private String description;

    @Column
    private BigDecimal price;
}
