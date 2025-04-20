package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.PublicationTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationTagRepository extends JpaRepository<PublicationTagEntity, Integer> {

    List<PublicationTagEntity> findAllByPublicationId(Integer id);

    void deleteAllByPublicationId(Integer id);
}
