package com.dm.service;

import com.dm.data.repository.TimeslotRepository;
import com.dm.dto.TimeslotDto;
import com.dm.mapper.TimeslotMapper;
import com.dm.model.Timeslot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeslotService {

    private final TimeslotRepository repository;
    private final TimeslotMapper mapper;

    public TimeslotService(TimeslotRepository repository, TimeslotMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<TimeslotDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TimeslotDto> findByDay(String day) {
        return repository.findAllByDay(day).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public TimeslotDto findByDayAndStart(String day, String startTime) {
        Timeslot slot = repository.findByDayAndStartTime(day, startTime);
        return slot != null ? mapper.toDto(slot) : null;
    }

    public TimeslotDto save(TimeslotDto dto) {
        Timeslot slot = mapper.toEntity(dto);
        return mapper.toDto(repository.save(slot));
    }
}