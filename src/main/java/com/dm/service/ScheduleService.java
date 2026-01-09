package com.dm.service;

import com.dm.data.repository.ScheduleEntryRepository;
import com.dm.dto.ScheduleEntryDto;
import com.dm.mapper.ScheduleEntryMapper;
import com.dm.model.types.ScheduleStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing scheduled entries (course assignments to timeslots and rooms).
 */
@Service
public class ScheduleService {

    private final ScheduleEntryRepository repository;
    private final ScheduleEntryMapper mapper;

    public ScheduleService(ScheduleEntryRepository repository, ScheduleEntryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Get all scheduled entries.
     */
    public List<ScheduleEntryDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get schedule for a specific teacher.
     */
    public List<ScheduleEntryDto> getByTeacherId(Long teacherId) {
        return repository.findAllByOfferingTeacherId(teacherId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get schedule for a specific group.
     */
    public List<ScheduleEntryDto> getByGroupId(Long groupId) {
        return repository.findAllByOfferingGroupId(groupId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get schedule entries for a specific day.
     */
    public List<ScheduleEntryDto> getByDay(DayOfWeek day) {
        return repository.findAllByTimeslotDayOfWeek(day).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get schedule entries for a specific room.
     */
    public List<ScheduleEntryDto> getByRoomId(Long roomId) {
        return repository.findAllByRoomId(roomId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get confirmed entries only.
     */
    public List<ScheduleEntryDto> getConfirmedOnly() {
        return repository.findAllByStatus(ScheduleStatus.CONFIRMED).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Create new schedule entry.
     */
    public ScheduleEntryDto save(ScheduleEntryDto dto) {
        var entity = new com.dm.data.entity.ScheduleEntryEntity();
        // Manual mapping since DTO is simpler for creation
        entity.setWeekPattern(dto.getWeekPattern());
        entity.setStatus(dto.getStatus());
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<ScheduleEntryDto> getByTeacherEmail(String email) {
        // This would require joining ScheduleEntry -> CourseOffering -> Teacher -> User
        // For now, return empty list - implement with proper repository query if needed
        return List.of();
    }
}