package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Integer> {

    Optional<OptionEntity> findByOptionName(String optionName);
}
