package com.dm.data.repository;

import com.dm.data.entity.ScheduleBackupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleBackupRepository extends JpaRepository<ScheduleBackupEntity, Long> {
    List<ScheduleBackupEntity> findByGroupId(Long groupId);
}
