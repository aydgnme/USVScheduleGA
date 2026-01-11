package com.dm.data.repository;

import com.dm.data.entity.SpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecializationRepository extends JpaRepository<SpecializationEntity, Long> {
    Optional<SpecializationEntity> findByCode(String code);

    List<SpecializationEntity> findByDepartmentId(Long departmentId);

    List<SpecializationEntity> findByStudyCycle(String studyCycle);

    @org.springframework.data.jpa.repository.Query("SELECT s FROM SpecializationEntity s JOIN FETCH s.department")
    List<SpecializationEntity> findAllWithDepartment();
}
