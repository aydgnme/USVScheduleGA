package com.dm.data.repository;

import com.dm.data.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByCode(String code);

    List<Course> findAllByType(String type);

    List<Course> findAllByParity(String parity);
    List<Course> findAllByTeacherId(Long teacherId);

    List<Course> findAllByGroupId(Long groupId);

    String teacher_Id(Long teacherId);
}
