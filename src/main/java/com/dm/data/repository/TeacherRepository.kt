package com.dm.data.repository

import com.dm.model.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeacherRepository : JpaRepository<Teacher, Long> {

    fun findByEmail(email: String): Teacher?

    fun findAllByDepartment(department: String): List<Teacher>

    fun findAllByAvailableDaysContaining(day: String): List<Teacher>
}