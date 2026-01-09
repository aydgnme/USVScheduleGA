package com.dm.data.repository;

import com.dm.data.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    Optional<DepartmentEntity> findByCode(String code);
    List<DepartmentEntity> findByFacultyId(Long facultyId);
}
