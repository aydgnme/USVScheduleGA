package com.dm.data.repository;

import com.dm.data.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for GroupEntity.
 */
@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    Optional<GroupEntity> findByCode(String code);

    java.util.List<GroupEntity> findBySpecializationId(Long specializationId);

    java.util.List<GroupEntity> findByStudyYear(Integer studyYear);

    java.util.List<GroupEntity> findByIsModular(Integer isModular);

    java.util.List<GroupEntity> findBySpecialization_Department_Id(Long departmentId);
}
