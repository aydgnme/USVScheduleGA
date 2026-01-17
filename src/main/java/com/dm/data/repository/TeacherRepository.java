package com.dm.data.repository;

import com.dm.data.entity.TeacherProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for TeacherProfileEntity.
 */
@Repository
public interface TeacherRepository extends JpaRepository<TeacherProfileEntity, Long> {

    @Query("SELECT tp FROM TeacherProfileEntity tp WHERE tp.user.email = :email")
    Optional<TeacherProfileEntity> findByUserEmail(String email);

    @Query("SELECT tp FROM TeacherProfileEntity tp JOIN tp.departments d WHERE d.id = :departmentId")
    java.util.List<TeacherProfileEntity> findByDepartmentId(Long departmentId);

    @Query("SELECT tp FROM TeacherProfileEntity tp JOIN tp.departments d WHERE d.code = :departmentCode")
    java.util.List<TeacherProfileEntity> findByDepartmentCode(String departmentCode);
}

