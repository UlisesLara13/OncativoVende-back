package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.PublicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity, Integer>, JpaSpecificationExecutor<PublicationEntity> {

    List<PublicationEntity> findTop10ByActiveTrueOrderByCreatedAtDesc();

    Optional<PublicationEntity> findByIdAndActiveTrue(Integer id);

    List<PublicationEntity> findAllByUser_Id(Integer userId);


    @Query("SELECT DISTINCT p FROM PublicationEntity p " +
            "LEFT JOIN PublicationCategoryEntity pc ON p.id = pc.publication.id " +
            "LEFT JOIN CategoryEntity c ON pc.categoryEntity.id = c.id " +
            "LEFT JOIN PublicationTagEntity pt ON p.id = pt.publication.id " +
            "LEFT JOIN TagEntity t ON pt.tag.id = t.id " +
            "WHERE (:active IS NULL OR p.active = :active) " + // ← esta es la nueva línea
            "AND (" +
            "   :searchTerm IS NULL OR " +
            "   LOWER(p.title) LIKE %:searchTerm% OR " +
            "   LOWER(p.description) LIKE %:searchTerm% OR " +
            "   LOWER(p.user.name) LIKE %:searchTerm% OR " +
            "   LOWER(p.user.surname) LIKE %:searchTerm% OR " +
            "   LOWER(p.location_id.description) LIKE %:searchTerm% OR " +
            "   LOWER(c.description) LIKE %:searchTerm% OR " +
            "   LOWER(t.description) LIKE %:searchTerm%" +
            ") " +
            "AND (:categories IS NULL OR LOWER(c.description) IN :categories) " +
            "AND (:tags IS NULL OR LOWER(t.description) IN :tags) " +
            "AND (:location IS NULL OR LOWER(p.location_id.description) LIKE %:location%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<PublicationEntity> findPublicationsWithAllFilters(
            @Param("searchTerm") String searchTerm,
            @Param("categories") List<String> categories,
            @Param("tags") List<String> tags,
            @Param("location") String location,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("active") Boolean active,
            Pageable pageable);

    @Query("SELECT p FROM PublicationEntity p " +
            "WHERE p.user.id = :userId " +
            "AND (:active IS NULL OR p.active = :active) " +
            "AND (" +
            "   :searchTerm IS NULL OR " +
            "   LOWER(p.title) LIKE %:searchTerm% OR " +
            "   LOWER(p.description) LIKE %:searchTerm%" +
            ")")
    Page<PublicationEntity> findUserPublicationsWithFilters(
            @Param("userId") Integer userId,
            @Param("searchTerm") String searchTerm,
            @Param("active") Boolean active,
            Pageable pageable);


    Long countByCreatedAtBetween(LocalDate from, LocalDate to);

    Long countByActive(Boolean active);
    Long countByActiveAndCreatedAtBetween(Boolean active, LocalDate from, LocalDate to);

    @Query("SELECT SUM(p.views) FROM PublicationEntity p")
    Long getTotalViews();

    @Query("SELECT SUM(p.views) FROM PublicationEntity p WHERE p.createdAt BETWEEN :from AND :to")
    Long getTotalViewsBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("SELECT AVG(p.price) FROM PublicationEntity p")
    BigDecimal getAveragePrice();

    @Query("SELECT AVG(p.price) FROM PublicationEntity p WHERE p.createdAt BETWEEN :from AND :to")
    BigDecimal getAveragePriceBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("""
        SELECT p.location_id.description, COUNT(p)
        FROM PublicationEntity p
        WHERE (:from IS NULL OR p.createdAt >= :from)
          AND (:to IS NULL OR p.createdAt <= :to)
        GROUP BY p.location_id.description
    """)
    List<Object[]> countByLocation(@Param("from") LocalDate from, @Param("to") LocalDate to);

}

