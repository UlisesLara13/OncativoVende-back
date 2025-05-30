package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional <UserEntity> findByUsername(String username);

    Optional <UserEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    UserEntity findTopByOrderByIdDesc();

}
