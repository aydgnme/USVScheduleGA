package com.dm.data.repository;

import com.dm.data.entity.ScheduleEntryEntity;
import com.dm.model.types.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

/**
 * Repository for ScheduleEntryEntity.
 */
@Repository
public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntryEntity, Long> {

        List<ScheduleEntryEntity> findAllByOfferingId(Long offeringId);

        List<ScheduleEntryEntity> findAllByTimeslotDayOfWeek(DayOfWeek day);

        List<ScheduleEntryEntity> findAllByRoomId(Long roomId);

        List<ScheduleEntryEntity> findAllByStatus(ScheduleStatus status);

        List<ScheduleEntryEntity> findAllByOfferingTeacherId(Long teacherId);

        List<ScheduleEntryEntity> findAllByOfferingGroupId(Long groupId);
}
