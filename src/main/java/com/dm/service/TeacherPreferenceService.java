package com.dm.service;

import com.dm.data.entity.TeacherPreferenceEntity;
import com.dm.data.entity.TeacherProfileEntity;
import com.dm.data.repository.TeacherPreferenceRepository;
import com.dm.data.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TeacherPreferenceService {

    private final TeacherPreferenceRepository repository;
    private final TeacherRepository teacherRepository;

    public TeacherPreferenceService(TeacherPreferenceRepository repository, TeacherRepository teacherRepository) {
        this.repository = repository;
        this.teacherRepository = teacherRepository;
    }

    public List<TeacherPreferenceEntity> getPreferencesByTeacherId(Long teacherId) {
        return repository.findByTeacherId(teacherId);
    }

    public List<TeacherPreferenceEntity> getPreferencesByTeacherEmail(String email) {
        return repository.findByTeacher_User_Email(email);
    }

    @Transactional
    public TeacherPreferenceEntity save(TeacherPreferenceEntity preference, String teacherEmail) {
        TeacherProfileEntity teacher = teacherRepository.findByUserEmail(teacherEmail)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found for email: " + teacherEmail));
        preference.setTeacher(teacher);
        return repository.save(preference);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
