package com.dm.data.repository

import com.dm.model.Timeslot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TimeslotRepository : JpaRepository<Timeslot, Long> {

    fun findAllByDay(day: String): List<Timeslot>

    fun findByDayAndStartTime(day: String, startTime: String): Timeslot?

    fun findAllByStartTime(startTime: String): List<Timeslot>

    fun findAllByEndTime(endTime: String): List<Timeslot>
}