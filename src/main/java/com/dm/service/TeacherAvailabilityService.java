package com.dm.service;

import com.dm.data.entity.TeacherPreferenceEntity;
import com.dm.data.entity.TeacherProfileEntity;
import com.dm.data.repository.TeacherPreferenceRepository;
import com.dm.data.repository.TeacherRepository;
import com.dm.dto.TeacherPreferenceDto;
import com.dm.model.types.PreferenceType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeacherAvailabilityService {

    private final TeacherPreferenceRepository repository;
    private final TeacherRepository teacherRepository;

    public TeacherAvailabilityService(TeacherPreferenceRepository repository, TeacherRepository teacherRepository) {
        this.repository = repository;
        this.teacherRepository = teacherRepository;
    }

    public List<TeacherPreferenceDto> getPreferences(Long teacherId) {
        return repository.findByTeacherId(teacherId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TeacherPreferenceDto savePreference(TeacherPreferenceDto dto) {
        TeacherPreferenceEntity entity;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(new TeacherPreferenceEntity());
        } else {
            entity = new TeacherPreferenceEntity();
        }

        TeacherProfileEntity teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        entity.setTeacher(teacher);
        entity.setDayOfWeek(dto.getDayOfWeek());
        entity.setStartHour(dto.getStartHour());
        entity.setEndHour(dto.getEndHour());
        entity.setType(dto.getType());
        entity.setPriority(dto.getPriority());
        // Course/Group are null for general availability
        entity.setCourse(null);
        entity.setGroup(null);

        TeacherPreferenceEntity saved = repository.save(entity);
        return toDto(saved);
    }

    public void deletePreference(Long id) {
        repository.deleteById(id);
    }

    private TeacherPreferenceDto toDto(TeacherPreferenceEntity entity) {
        TeacherPreferenceDto dto = new TeacherPreferenceDto();
        dto.setId(entity.getId());
        dto.setTeacherId(entity.getTeacher().getId());
        dto.setDayOfWeek(entity.getDayOfWeek());
        dto.setStartHour(entity.getStartHour());
        dto.setEndHour(entity.getEndHour());
        dto.setType(entity.getType());
        dto.setPriority(entity.getPriority());
        return dto;
    }
}
