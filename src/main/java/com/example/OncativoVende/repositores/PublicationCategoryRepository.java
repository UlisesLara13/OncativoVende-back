package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.PublicationCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationCategoryRepository extends JpaRepository<PublicationCategoryEntity, Integer> {

    List<PublicationCategoryEntity> findAllByPublicationId(Integer id);

    void deleteAllByPublicationId(Integer id);

}
