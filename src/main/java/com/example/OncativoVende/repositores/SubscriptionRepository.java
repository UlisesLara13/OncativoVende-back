package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Integer> {

}
