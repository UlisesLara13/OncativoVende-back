package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.PublicationTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationTagRepository extends JpaRepository<PublicationTagEntity, Integer> {

}
