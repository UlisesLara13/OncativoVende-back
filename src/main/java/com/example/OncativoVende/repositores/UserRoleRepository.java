package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {
    UserRoleEntity findByUserId(Integer userId);
    List<UserRoleEntity> findAllByUser_Id(Integer userId);
}
