package com.dm.service;

import com.dm.data.entity.TeacherProfileEntity;
import com.dm.data.repository.TeacherRepository;
import com.dm.dto.TeacherDto;
import com.dm.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing teacher profiles.
 */
@Service
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository repository;
    private final TeacherMapper mapper;

    public TeacherService(TeacherRepository repository, TeacherMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<TeacherDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public TeacherDto findByEmail(String email) {
        return repository.findByUserEmail(email)
                .map(mapper::toDto)
                .orElse(null);
    }

    public TeacherDto findByUserEmail(String email) {
        return repository.findByUserEmail(email)
                .map(mapper::toDto)
                .orElse(null);
    }

    public List<TeacherDto> findByDepartmentId(Long departmentId) {
        return repository.findByDepartmentId(departmentId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TeacherDto> findByDepartmentCode(String departmentCode) {
        return repository.findByDepartmentCode(departmentCode)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional
    public TeacherDto save(TeacherDto dto) {
        TeacherProfileEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @org.springframework.transaction.annotation.Transactional
    public TeacherDto update(Long id, TeacherDto dto) {
        TeacherProfileEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + id));
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        // Note: departments mapping is handled via MapStruct or manual entity
        // processing if needed
        entity.setMaxHoursWeekly(dto.getMaxHoursWeekly());
        entity.setAvailableDaysJson(dto.getAvailableDaysJson());
        entity.setPreferredTime(dto.getPreferredTime());
        entity.setNote(dto.getNote());
        return mapper.toDto(repository.save(entity));
    }

    @org.springframework.transaction.annotation.Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }
}