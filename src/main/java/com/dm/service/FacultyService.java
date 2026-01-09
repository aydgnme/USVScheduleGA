package com.dm.service;

import com.dm.data.entity.FacultyEntity;
import com.dm.data.repository.FacultyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public List<FacultyEntity> getAll() {
        return facultyRepository.findAll();
    }

    public Optional<FacultyEntity> findById(Long id) {
        return facultyRepository.findById(id);
    }

    public Optional<FacultyEntity> findByCode(String code) {
        return facultyRepository.findByCode(code);
    }

    public FacultyEntity create(FacultyEntity faculty) {
        return facultyRepository.save(faculty);
    }

    public FacultyEntity update(Long id, FacultyEntity facultyData) {
        return facultyRepository.findById(id).map(faculty -> {
            faculty.setCode(facultyData.getCode());
            faculty.setName(facultyData.getName());
            faculty.setShortName(facultyData.getShortName());
            return facultyRepository.save(faculty);
        }).orElseThrow(() -> new RuntimeException("Faculty not found"));
    }

    public void delete(Long id) {
        facultyRepository.deleteById(id);
    }
}
