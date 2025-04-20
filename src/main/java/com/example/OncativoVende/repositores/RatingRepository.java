package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.RatingEntity;
import com.example.OncativoVende.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
    List<RatingEntity> findAllByRatedUserId(Integer userId);
    Integer countByRatedUserId(Integer userId);
}
