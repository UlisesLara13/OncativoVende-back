package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.PublicationCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PublicationCategoryRepository extends JpaRepository<PublicationCategoryEntity, Integer> {

    List<PublicationCategoryEntity> findAllByPublicationId(Integer id);

    void deleteAllByPublicationId(Integer id);

    @Query("""
        SELECT pc.categoryEntity.description, COUNT(pc)
        FROM PublicationCategoryEntity pc
        WHERE (:from IS NULL OR pc.publication.createdAt >= :from)
          AND (:to IS NULL OR pc.publication.createdAt <= :to)
        GROUP BY pc.categoryEntity.description
    """)
    List<Object[]> countByCategoryBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);

}
