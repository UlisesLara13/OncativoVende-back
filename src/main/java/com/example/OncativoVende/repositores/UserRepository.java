package com.example.OncativoVende.repositores;

import com.example.OncativoVende.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional <UserEntity> findByUsername(String username);

    Optional <UserEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByEmailAndActiveFalse(String email);

    boolean existsByUsernameAndActiveFalse(String username);

    @Query("SELECT DISTINCT u FROM UserEntity u " +
            "JOIN UserRoleEntity ur ON ur.user.id = u.id " +
            "JOIN RoleEntity r ON ur.role.id = r.id " +
            "LEFT JOIN LocationEntity l ON u.location_id.id = l.id " +
            "WHERE (:searchTerm IS NULL OR " +
            "   LOWER(u.name) LIKE %:searchTerm% OR " +
            "   LOWER(u.surname) LIKE %:searchTerm% OR " +
            "   LOWER(u.username) LIKE %:searchTerm% OR " +
            "   LOWER(u.email) LIKE %:searchTerm% OR " +
            "   LOWER(l.description) LIKE %:searchTerm% OR " +
            "   LOWER(r.description) LIKE %:searchTerm%) " +
            "AND (:active IS NULL OR u.active = :active) " +
            "AND (:verified IS NULL OR u.verified = :verified) " +
            "AND (:roleDescriptions IS NULL OR LOWER(r.description) IN :roleDescriptions) " +
            "AND (:location IS NULL OR LOWER(l.description) LIKE %:location%)")
    Page<UserEntity> findUsersWithFilters(
            @Param("searchTerm") String searchTerm,
            @Param("active") Boolean active,
            @Param("verified") Boolean verified,
            @Param("roleDescriptions") List<String> roleDescriptions,
            @Param("location") String location,
            Pageable pageable);

}
