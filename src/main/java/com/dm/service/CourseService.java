package com.dm.service;

import com.dm.data.repository.CourseRepository;
import com.dm.dto.CourseDto;
import com.dm.mapper.CourseMapper;
import com.dm.data.entity.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository repository;
    private final CourseMapper mapper;

    public CourseService(CourseRepository repository, CourseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CourseDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CourseDto findByCode(String code) {
        Course course = repository.findByCode(code);
        return course != null ? mapper.toDto(course) : null;
    }

    public List<CourseDto> findByTeacher(Long teacherId) {
        return repository.findAllByTeacherId(teacherId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> findByGroup(Long groupId) {
        return repository.findAllByGroupId(groupId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CourseDto save(CourseDto dto) {
        Course course = mapper.toEntity(dto);
        return mapper.toDto(repository.save(course));
    }

    public List<CourseDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(CourseDto course) {
        repository.delete(mapper.toEntity(course));
    }
}