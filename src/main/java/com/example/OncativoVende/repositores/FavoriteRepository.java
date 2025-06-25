package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository <FavoriteEntity , Integer> {

    boolean existsByPublicationIdAndUserId(Integer publicationId, Integer userId);

    Optional<FavoriteEntity> findByPublicationIdAndUserId(Integer publicationId, Integer userId);

    List<FavoriteEntity> findByUserIdOrderByIdDesc(Integer userId);
}
