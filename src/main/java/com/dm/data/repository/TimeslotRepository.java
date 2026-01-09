package com.dm.data.repository;

import com.dm.data.entity.TimeslotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for TimeslotEntity.
 */
@Repository
public interface TimeslotRepository extends JpaRepository<TimeslotEntity, Long> {

    List<TimeslotEntity> findAllByDayOfWeek(DayOfWeek dayOfWeek);

    Optional<TimeslotEntity> findByDayOfWeekAndStartTimeAndEndTime(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime);
}
