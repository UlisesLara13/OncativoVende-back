package com.example.OncativoVende.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "options")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class OptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "option_name", unique = true, nullable = false)
    private String optionName;

    @Column(name = "value", nullable = false)
    private Integer value;
}
