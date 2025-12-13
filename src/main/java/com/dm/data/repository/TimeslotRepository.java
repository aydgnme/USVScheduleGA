package com.dm.data.repository;

import com.dm.data.entity.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

    List<Timeslot> findAllByDay(String day);

    Timeslot findByDayAndStartTimeAndEndTime(String day, String startTime, String endTime);

    Timeslot findByDayAndStartTime(String day, String startTime);
}
