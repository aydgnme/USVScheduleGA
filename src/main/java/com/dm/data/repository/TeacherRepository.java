package com.dm.data.repository;

import com.dm.data.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByEmail(String email);

    List<Teacher> findAllByDepartment(String department);

    List<Teacher> findAllByAvailableDaysContaining(String day);
}
