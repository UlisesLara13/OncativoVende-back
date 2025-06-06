package com.example.OncativoVende.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location_id;

    @Column
    private Boolean active;

    @Column
    private Boolean verified;

    @Column
    private String avatar_url;

    @Column(length = 6)
    private String recovery_code;

    @Column
    private LocalDateTime recovery_code_expiration;

    @Column
    private LocalDate created_at;
}
