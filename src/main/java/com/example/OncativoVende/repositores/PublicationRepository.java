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

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity, Integer>, JpaSpecificationExecutor<PublicationEntity> {

    List<PublicationEntity> findTop10ByActiveTrueOrderByCreatedAtDesc();


    @Query("SELECT DISTINCT p FROM PublicationEntity p " +
            "LEFT JOIN PublicationCategoryEntity pc ON p.id = pc.publication.id " +
            "LEFT JOIN CategoryEntity c ON pc.categoryEntity.id = c.id " +
            "LEFT JOIN PublicationTagEntity pt ON p.id = pt.publication.id " +
            "LEFT JOIN TagEntity t ON pt.tag.id = t.id " +
            "WHERE p.active = true " +
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
            Pageable pageable);



}

