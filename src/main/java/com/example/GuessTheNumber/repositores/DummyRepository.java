package com.example.GuessTheNumber.repositores;

import com.example.GuessTheNumber.entities.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyRepository extends JpaRepository<DummyEntity, Long> {
}
