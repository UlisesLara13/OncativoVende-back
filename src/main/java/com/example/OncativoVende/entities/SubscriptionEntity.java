package com.example.OncativoVende.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user_id;

    @ManyToOne
    @JoinColumn(name = "subscription_type_id", referencedColumnName = "id")
    private SubscriptionTypeEntity subscription_type_id;

    @Column
    private LocalDate start_date;

    @Column
    private LocalDate end_date;

    @Column
    private Integer discount_applied;

    @Column
    private BigDecimal total_price;

}
