package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.SubscriptionEntity;
import com.example.OncativoVende.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Integer> {

    SubscriptionEntity findByUserIdAndEndDateAfter(UserEntity userId, LocalDate endDate);

    @Query("""
        SELECT COUNT(s) 
        FROM SubscriptionEntity s 
        WHERE s.userId.id = :userId 
          AND s.endDate >= :today
    """)
    Long countActiveSubscriptions(@Param("userId") Integer userId, @Param("today") LocalDate today);

    @Query("""
        SELECT COUNT(s)
        FROM SubscriptionEntity s
        WHERE s.startDate BETWEEN :from AND :to
    """)
    Long countByStartDateBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("""
        SELECT SUM(s.total_price)
        FROM SubscriptionEntity s
        WHERE s.startDate BETWEEN :from AND :to
    """)
    BigDecimal sumRevenueByStartDateBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("""
        SELECT COUNT(s)
        FROM SubscriptionEntity s
        WHERE s.discount_applied > 0 AND s.startDate BETWEEN :from AND :to
    """)
    Long countWithDiscount(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("""
        SELECT COUNT(s)
        FROM SubscriptionEntity s
        WHERE (s.discount_applied = 0 OR s.discount_applied IS NULL) AND s.startDate BETWEEN :from AND :to
    """)
    Long countWithoutDiscount(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("""
        SELECT COUNT(s)
        FROM SubscriptionEntity s
        WHERE s.endDate >= :today AND s.startDate BETWEEN :from AND :to
    """)
    Long countActiveSubscriptions(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("today") LocalDate today);

    @Query("""
        SELECT COUNT(s)
        FROM SubscriptionEntity s
        WHERE s.endDate < :today AND s.startDate BETWEEN :from AND :to
    """)
    Long countInactiveSubscriptions(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("today") LocalDate today);

    @Query("""
        SELECT FUNCTION('MONTH', s.startDate), COUNT(s)
        FROM SubscriptionEntity s
        WHERE s.startDate BETWEEN :from AND :to
        GROUP BY FUNCTION('MONTH', s.startDate)
        ORDER BY FUNCTION('MONTH', s.startDate)
    """)
    List<Object[]> countSubscriptionsByMonth(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
