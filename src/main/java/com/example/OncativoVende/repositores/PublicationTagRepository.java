package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.PublicationTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PublicationTagRepository extends JpaRepository<PublicationTagEntity, Integer> {

    List<PublicationTagEntity> findAllByPublicationId(Integer id);

    void deleteAllByPublicationId(Integer id);

    @Query("""
        SELECT pt.tag.description, COUNT(pt)
        FROM PublicationTagEntity pt
        WHERE (:from IS NULL OR pt.publication.createdAt >= :from)
          AND (:to IS NULL OR pt.publication.createdAt <= :to)
        GROUP BY pt.tag.description
    """)
    List<Object[]> countByTagBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
