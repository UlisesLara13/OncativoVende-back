package com.example.OncativoVende.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reported_by_user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "publication_id")
    private PublicationEntity publication;

    @Column
    private String reason;

    @Column
    private LocalDate created_at;

    @Column
    private String status;

    @Column
    private String response;

}
