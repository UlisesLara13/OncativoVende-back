package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.PublicationImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationImageRepository extends JpaRepository<PublicationImageEntity, Integer> {

}
