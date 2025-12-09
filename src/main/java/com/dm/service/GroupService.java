package com.dm.service;

import com.dm.data.repository.GroupRepository;
import com.dm.dto.GroupDto;
import com.dm.mapper.GroupMapper;
import com.dm.model.Group;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public GroupDto findByName(String name) {
        Group group = repository.findByName(name);
        return group != null ? mapper.toDto(group) : null;
    }

    public List<GroupDto> findByYear(int year) {
        return repository.findAllByStudyYear(year).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> findByFaculty(String faculty) {
        return repository.findAllByFaculty(faculty).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public GroupDto save(GroupDto dto) {
        Group group = mapper.toEntity(dto);
        return mapper.toDto(repository.save(group));
    }
}