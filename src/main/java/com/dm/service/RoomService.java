package com.dm.service;

import com.dm.data.repository.RoomRepository;
import com.dm.dto.RoomDto;
import com.dm.mapper.RoomMapper;
import com.dm.model.Room;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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

    public RoomDto findByCode(String code) {
        Room room = repository.findByCode(code);
        return room != null ? mapper.toDto(room) : null;
    }

    public List<RoomDto> findByType(String type) {
        return repository.findAllByType(type).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RoomDto> findByCapacity(int minCapacity) {
        return repository.findAllByCapacityGreaterThanEqual(minCapacity).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public RoomDto save(RoomDto dto) {
        Room room = mapper.toEntity(dto);
        return mapper.toDto(repository.save(room));
    }
}