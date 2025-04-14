package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {

}
