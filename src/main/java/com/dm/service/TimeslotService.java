package com.dm.service;

import com.dm.data.entity.TimeslotEntity;
import com.dm.data.repository.TimeslotRepository;
import com.dm.dto.TimeslotDto;
import com.dm.mapper.TimeslotMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing timeslots.
 */
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

    public List<TimeslotDto> findByDay(DayOfWeek day) {
        return repository.findAllByDayOfWeek(day).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public TimeslotDto findByDayAndTime(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        return repository.findByDayOfWeekAndStartTimeAndEndTime(day, startTime, endTime)
                .map(mapper::toDto)
                .orElse(null);
    }

    public TimeslotDto save(TimeslotDto dto) {
        TimeslotEntity entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
