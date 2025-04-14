package com.example.OncativoVende.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Column
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location_id;

}
