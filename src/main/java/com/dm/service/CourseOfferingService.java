package com.dm.service;

import com.dm.data.entity.CourseOfferingEntity;
import com.dm.data.repository.CourseOfferingRepository;
import com.dm.data.repository.CourseRepository;
import com.dm.data.repository.GroupRepository;
import com.dm.data.repository.TeacherRepository;
import com.dm.dto.CourseOfferingDto;
import com.dm.mapper.CourseOfferingMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing course offerings (assignments of courses to groups/teachers).
 */
@Service
public class CourseOfferingService {

    private final CourseOfferingRepository repository;
    private final CourseRepository courseRepo;
    private final GroupRepository groupRepo;
    private final TeacherRepository teacherRepo;
    private final CourseOfferingMapper mapper;

    public CourseOfferingService(
            CourseOfferingRepository repository,
            CourseRepository courseRepo,
            GroupRepository groupRepo,
            TeacherRepository teacherRepo,
            CourseOfferingMapper mapper) {
        this.repository = repository;
        this.courseRepo = courseRepo;
        this.groupRepo = groupRepo;
        this.teacherRepo = teacherRepo;
        this.mapper = mapper;
    }

    public List<CourseOfferingDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CourseOfferingDto> getByCourseId(Long courseId) {
        return repository.findAllByCourseId(courseId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CourseOfferingDto> getByGroupId(Long groupId) {
        return repository.findAllByGroupId(groupId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CourseOfferingDto> getByTeacherId(Long teacherId) {
        return repository.findAllByTeacherId(teacherId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CourseOfferingDto save(CourseOfferingDto dto) {
        CourseOfferingEntity entity = new CourseOfferingEntity();
        entity.setCourse(courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + dto.getCourseId())));
        entity.setGroup(groupRepo.findById(dto.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found: " + dto.getGroupId())));
        entity.setTeacher(teacherRepo.findById(dto.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + dto.getTeacherId())));
        entity.setWeeklyHours(dto.getWeeklyHours());
        entity.setParity(dto.getParity());
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
