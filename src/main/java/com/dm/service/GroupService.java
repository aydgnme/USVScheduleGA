package com.dm.service;

import com.dm.data.entity.GroupEntity;
import com.dm.data.repository.GroupRepository;
import com.dm.dto.GroupDto;
import com.dm.mapper.GroupMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing groups.
 */
@Service
public class GroupService {

    private final GroupRepository repository;
    private final GroupMapper mapper;

    public GroupService(GroupRepository repository, GroupMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<GroupDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public GroupDto findByCode(String code) {
        return repository.findByCode(code)
                .map(mapper::toDto)
                .orElse(null);
    }

    public List<GroupDto> findBySpecializationId(Long specializationId) {
        return repository.findBySpecializationId(specializationId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> findByStudyYear(Integer studyYear) {
        return repository.findByStudyYear(studyYear).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> findByIsModular(Integer isModular) {
        return repository.findByIsModular(isModular).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public GroupDto save(GroupDto dto) {
        GroupEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

