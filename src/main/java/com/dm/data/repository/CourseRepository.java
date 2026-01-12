package com.dm.data.repository;

import com.dm.data.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CourseEntity.
 */
@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    Optional<CourseEntity> findByCode(String code);

    boolean existsByCode(String code);

    List<CourseEntity> findByDepartmentId(Long departmentId);

    List<CourseEntity> findByDepartment_Faculty_Id(Long facultyId);
}
