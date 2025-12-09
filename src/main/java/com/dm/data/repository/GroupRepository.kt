package com.dm.data.repository

import com.dm.model.Group
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : JpaRepository<Group, Long> {

    fun findByName(name: String): Group?

    fun findAllByStudyYear(studyYear: Int): List<Group>

    fun findAllBySpecialization(specialization: String): List<Group>

    fun findAllByFaculty(faculty: String): List<Group>
}