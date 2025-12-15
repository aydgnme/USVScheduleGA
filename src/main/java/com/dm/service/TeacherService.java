package com.dm.service;

import com.dm.data.entity.Teacher;
import com.dm.data.repository.TeacherRepository;
import com.dm.dto.TeacherDto;
import com.dm.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        Teacher teacher = repository.findByEmail(email);
        return teacher != null ? mapper.toDto(teacher) : null;
    }

    public List<TeacherDto> findByDepartment(String department) {
        return repository.findAllByDepartment(department)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TeacherDto> findAvailableByDay(String day) {
        return repository.findAllByAvailableDaysContaining(day)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public TeacherDto save(TeacherDto dto) {
        Teacher teacher = mapper.toEntity(dto);
        return mapper.toDto(repository.save(teacher));
    }

    public TeacherDto update(TeacherDto dto) {
        return save(dto);
    }
}