package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.SubscriptionEntity;
import com.example.OncativoVende.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Integer> {

    SubscriptionEntity findByUserIdAndEndDateAfter(UserEntity userId, LocalDate endDate);

}
