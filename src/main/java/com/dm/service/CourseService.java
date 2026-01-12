package com.dm.service;

import com.dm.data.entity.CourseEntity;
import com.dm.data.repository.CourseRepository;
import com.dm.dto.CourseDto;
import com.dm.dto.CourseRequestDto;
import com.dm.mapper.CourseMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing courses (definitions only; assignments via
 * CourseOfferingService).
 */
@Service
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository repository;
    private final com.dm.data.repository.CourseOfferingRepository offeringRepository;
    private final CourseMapper mapper;

    public CourseService(CourseRepository repository,
            com.dm.data.repository.CourseOfferingRepository offeringRepository, CourseMapper mapper,
            com.dm.data.repository.DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.offeringRepository = offeringRepository;
        this.mapper = mapper;
        this.departmentRepository = departmentRepository;
    }

    private final com.dm.data.repository.DepartmentRepository departmentRepository;

    public List<CourseDto> findByDepartment(Long departmentId) {
        return repository.findByDepartmentId(departmentId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> findByFacultyId(Long facultyId) {
        return repository.findByDepartment_Faculty_Id(facultyId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> findAll() {
        return getAll();
    }

    public java.util.Optional<CourseDto> findById(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public CourseDto findByCode(String code) {
        return repository.findByCode(code)
                .map(mapper::toDto)
                .orElse(null);
    }

    public CourseDto save(CourseRequestDto request) {
        CourseEntity entity = mapper.toEntityFromRequest(request);
        return mapper.toDto(repository.save(entity));
    }

    public CourseDto save(CourseDto dto) {
        CourseEntity entity = mapper.toEntity(dto);
        if (dto.getDepartmentId() != null) {
            entity.setDepartment(departmentRepository.getReferenceById(dto.getDepartmentId()));
        }
        return mapper.toDto(repository.save(entity));
    }

    public CourseDto update(Long id, CourseRequestDto request) {
        CourseEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
        entity.setCode(request.getCode());
        entity.setTitle(request.getTitle());
        entity.setComponentType(request.getComponentType());
        entity.setCredits(request.getCredits());
        entity.setSemester(request.getSemester());
        entity.setParity(request.getParity());
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<CourseDto> getCoursesByTeacherEmail(String email) {
        return offeringRepository.findAllByTeacher_User_Email(email)
                .stream()
                .map(offering -> offering.getCourse())
                .distinct() // Return unique courses (even if taught to multiple groups)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public long count() {
        return repository.count();
    }
}