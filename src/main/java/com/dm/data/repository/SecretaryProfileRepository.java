package com.dm.data.repository;

import com.dm.data.entity.SecretaryProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecretaryProfileRepository extends JpaRepository<SecretaryProfileEntity, Long> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "department")
    Optional<SecretaryProfileEntity> findByUser_Email(String email);
}
