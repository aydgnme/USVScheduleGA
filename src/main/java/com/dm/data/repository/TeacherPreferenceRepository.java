package com.dm.data.repository;

import com.dm.data.entity.TeacherPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherPreferenceRepository extends JpaRepository<TeacherPreferenceEntity, Long> {
    List<TeacherPreferenceEntity> findByTeacherId(Long teacherId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = { "course", "group" })
    List<TeacherPreferenceEntity> findByTeacher_User_Email(String email);
}
