package com.example.OncativoVende.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ratings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class RatingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "rater_user_id")
    private UserEntity raterUser;

    @ManyToOne
    @JoinColumn(name = "rated_user_id")
    private UserEntity ratedUser;

    @ManyToOne
    @JoinColumn(name = "publication_id")
    private PublicationEntity publication;

    @Column
    private BigDecimal rating;

    @Column
    private String comment;

    @Column
    private LocalDate created_at;

}
