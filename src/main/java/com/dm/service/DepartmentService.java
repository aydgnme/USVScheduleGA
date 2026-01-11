package com.dm.service;

import com.dm.data.entity.DepartmentEntity;
import com.dm.data.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyService facultyService;

    public DepartmentService(DepartmentRepository departmentRepository, FacultyService facultyService) {
        this.departmentRepository = departmentRepository;
        this.facultyService = facultyService;
    }

    public List<DepartmentEntity> getAll() {
        return departmentRepository.findAllWithFaculty();
    }

    public Optional<DepartmentEntity> findById(Long id) {
        return departmentRepository.findById(id);
    }

    public Optional<DepartmentEntity> findByCode(String code) {
        return departmentRepository.findByCode(code);
    }

    public List<DepartmentEntity> findByFacultyId(Long facultyId) {
        return departmentRepository.findByFacultyId(facultyId);
    }

    public DepartmentEntity create(DepartmentEntity department) {
        return departmentRepository.save(department);
    }

    public DepartmentEntity update(Long id, DepartmentEntity departmentData) {
        return departmentRepository.findById(id).map(department -> {
            department.setCode(departmentData.getCode());
            department.setName(departmentData.getName());
            if (departmentData.getFaculty() != null) {
                department.setFaculty(departmentData.getFaculty());
            }
            return departmentRepository.save(department);
        }).orElseThrow(() -> new RuntimeException("Department not found"));
    }

    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
