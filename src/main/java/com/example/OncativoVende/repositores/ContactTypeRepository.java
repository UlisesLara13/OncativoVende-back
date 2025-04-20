package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.ContactTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactTypeRepository extends JpaRepository<ContactTypeEntity, Integer> {

}
