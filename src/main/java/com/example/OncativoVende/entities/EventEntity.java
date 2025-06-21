package com.example.OncativoVende.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column
    private String title;

    @Column(length = 500)
    private String description;

    @Column
    private String image_url;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", referencedColumnName = "id")
    private UserEntity created_by_user_id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "created_at")
    private LocalDate createdAt;

}
