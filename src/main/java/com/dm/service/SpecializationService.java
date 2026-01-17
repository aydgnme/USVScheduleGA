package com.dm.service;

import com.dm.data.entity.SpecializationEntity;
import com.dm.data.repository.SpecializationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public List<SpecializationEntity> getAll() {
        return specializationRepository.findAllWithDepartment();
    }

    public Optional<SpecializationEntity> findById(Long id) {
        return specializationRepository.findById(id);
    }

    public Optional<SpecializationEntity> findByCode(String code) {
        return specializationRepository.findByCode(code);
    }

    public List<SpecializationEntity> findByDepartmentId(Long departmentId) {
        return specializationRepository.findByDepartmentId(departmentId);
    }

    public List<SpecializationEntity> findByStudyCycle(String studyCycle) {
        return specializationRepository.findByStudyCycle(studyCycle);
    }

    public SpecializationEntity create(SpecializationEntity specialization) {
        return specializationRepository.save(specialization);
    }

    public SpecializationEntity update(Long id, SpecializationEntity specializationData) {
        return specializationRepository.findById(id).map(specialization -> {
            specialization.setCode(specializationData.getCode());
            specialization.setName(specializationData.getName());
            specialization.setStudyCycle(specializationData.getStudyCycle());
            if (specializationData.getDepartment() != null) {
                specialization.setDepartment(specializationData.getDepartment());
            }
            return specializationRepository.save(specialization);
        }).orElseThrow(() -> new RuntimeException("Specialization not found"));
    }

    public void delete(Long id) {
        specializationRepository.deleteById(id);
    }

    public long count() {
        return specializationRepository.count();
    }
}
