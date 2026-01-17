package com.dm.service;

import com.dm.data.entity.RoomEntity;
import com.dm.data.repository.RoomRepository;
import com.dm.dto.RoomDto;
import com.dm.mapper.RoomMapper;
import com.dm.model.types.RoomType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing rooms.
 */
@Service
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository repository;
    private final RoomMapper mapper;

    public RoomService(RoomRepository repository, RoomMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RoomDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RoomDto> findAll() {
        return getAll();
    }

    public RoomDto findByCode(String code) {
        return repository.findByCode(code)
                .map(mapper::toDto)
                .orElse(null);
    }

    public List<RoomDto> findByRoomType(RoomType roomType) {
        return repository.findAllByRoomType(roomType).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RoomDto> findByCapacity(int minCapacity) {
        return repository.findAllByCapacityGreaterThanEqual(minCapacity).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RoomDto> findByBuilding(String building) {
        return repository.findAllByBuilding(building).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RoomDto> findComputerLabsByBuilding(String building) {
        return repository.findAllByBuildingAndComputersGreaterThan(building, 0).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional
    public RoomDto save(RoomDto dto) {
        RoomEntity entity = mapper.toEntity(dto);
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
