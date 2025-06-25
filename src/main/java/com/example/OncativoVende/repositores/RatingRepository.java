package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.RatingEntity;
import com.example.OncativoVende.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
    List<RatingEntity> findAllByRatedUserId(Integer userId);
    List<RatingEntity> findAllByRatedUserIdOrderByCreatedAtDesc(Integer userId);
    Integer countByRatedUserId(Integer userId);
    RatingEntity findByRatedUserIdAndRaterUserId(Integer userId, Integer raterUserId);
}
