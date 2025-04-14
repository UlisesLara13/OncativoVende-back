package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Integer> {

}
