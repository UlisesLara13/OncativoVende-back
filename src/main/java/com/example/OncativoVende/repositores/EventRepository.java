package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository <EventEntity, Integer> {

    List<EventEntity> findByEndDateGreaterThanEqualOrderByCreatedAtDesc(LocalDate today);

    Optional<EventEntity> findFirstByOrderByCreatedAtDesc();
}
