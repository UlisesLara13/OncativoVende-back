package com.example.OncativoVende.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "publications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PublicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(length = 50)
    private String title;

    @Column(length = 500)
    private String description;

    @Column
    private BigDecimal price;

    @Column
    private Boolean active;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location_id;

}
