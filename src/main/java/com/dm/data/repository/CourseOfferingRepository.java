package com.dm.data.repository;

import com.dm.data.entity.CourseOfferingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for CourseOfferingEntity.
 */
@Repository
public interface CourseOfferingRepository extends JpaRepository<CourseOfferingEntity, Long> {

    List<CourseOfferingEntity> findAllByCourseId(Long courseId);

    List<CourseOfferingEntity> findAllByGroupId(Long groupId);

    List<CourseOfferingEntity> findAllByTeacherId(Long teacherId);

    @org.springframework.data.jpa.repository.Query("SELECT co FROM CourseOfferingEntity co JOIN FETCH co.course WHERE co.teacher.user.email = :email")
    List<CourseOfferingEntity> findAllByTeacher_User_Email(
            @org.springframework.data.repository.query.Param("email") String email);
}
