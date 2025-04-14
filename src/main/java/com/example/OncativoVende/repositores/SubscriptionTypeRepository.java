package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.SubscriptionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionTypeEntity, Integer> {

}
