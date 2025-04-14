package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.PublicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity, Integer> {

}
