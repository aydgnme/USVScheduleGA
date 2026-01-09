package com.dm.data.repository;

import com.dm.data.entity.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<FacultyEntity, Long> {
    Optional<FacultyEntity> findByCode(String code);
}
