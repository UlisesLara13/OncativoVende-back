package com.example.OncativoVende.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "publicationstags")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PublicationTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "publication_id")
    private PublicationEntity publication;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private TagEntity tag;
}
