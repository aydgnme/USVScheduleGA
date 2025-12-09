package com.dm.data.repository

import com.dm.model.Room
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoomRepository : JpaRepository<Room, Long> {

    fun findByCode(code: String): Room?

    fun findAllByType(type: String): List<Room>

    fun findAllByCapacityGreaterThanEqual(capacity: Int): List<Room>
}