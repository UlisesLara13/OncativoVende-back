package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository <FavoriteEntity , Integer> {

}
