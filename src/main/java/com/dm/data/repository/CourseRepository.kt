package com.dm.data.repository

import com.dm.model.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<Course, Long> {

    fun findByCode(code: String): Course?

    fun findAllByTeacher_Id(teacherId: Long): List<Course>

    fun findAllByGroup_Id(groupId: Long): List<Course>

    fun findAllByType(type: String): List<Course>
}