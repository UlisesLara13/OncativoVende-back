package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

    boolean existsByPublicationIdAndUserId(Integer publicationId, Integer userId);

    @Query("""
        SELECT r FROM ReportEntity r
        JOIN r.user u
        JOIN r.publication p
        WHERE (:searchTerm IS NULL OR
            LOWER(u.name) LIKE %:searchTerm% OR
            LOWER(u.surname) LIKE %:searchTerm% OR
            LOWER(u.location_id.description) LIKE %:searchTerm% OR
            LOWER(u.username) LIKE %:searchTerm% OR
            LOWER(u.email) LIKE %:searchTerm% OR
            LOWER(p.title) LIKE %:searchTerm% OR
            LOWER(p.description) LIKE %:searchTerm%)
        AND (:status IS NULL OR LOWER(r.status) = LOWER(:status))
    """)
    Page<ReportEntity> findReportsWithFilters(
            @Param("searchTerm") String searchTerm,
            @Param("status") String status,
            Pageable pageable
    );

}
