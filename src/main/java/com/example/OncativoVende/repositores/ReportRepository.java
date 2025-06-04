package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

    boolean existsByPublicationIdAndUserId(Integer publicationId, Integer userId);

}
