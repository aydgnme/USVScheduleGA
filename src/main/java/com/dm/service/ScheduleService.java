package com.dm.service;

import com.dm.data.repository.ScheduleEntryRepository;
import com.dm.dto.ScheduleEntryDto;
import com.dm.mapper.ScheduleEntryMapper;
import com.dm.model.types.ScheduleStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing scheduled entries (course assignments to timeslots and
 * rooms).
 */
@Transactional
@Service
public class ScheduleService {

    private final ScheduleEntryRepository repository;
    private final ScheduleEntryMapper mapper;

    private final com.dm.data.repository.CourseOfferingRepository offeringRepo;
    private final com.dm.data.repository.RoomRepository roomRepo;
    private final com.dm.data.repository.TimeslotRepository timeslotRepo;
    private final com.dm.data.repository.TeacherRepository teacherRepo;

    public ScheduleService(
            ScheduleEntryRepository repository,
            ScheduleEntryMapper mapper,
            com.dm.data.repository.CourseOfferingRepository offeringRepo,
            com.dm.data.repository.RoomRepository roomRepo,
            com.dm.data.repository.TimeslotRepository timeslotRepo,
            com.dm.data.repository.TeacherRepository teacherRepo) {
        this.repository = repository;
        this.mapper = mapper;
        this.offeringRepo = offeringRepo;
        this.roomRepo = roomRepo;
        this.timeslotRepo = timeslotRepo;
        this.teacherRepo = teacherRepo;
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
        var entity = (dto.getId() != null)
                ? repository.findById(dto.getId()).orElse(new com.dm.data.entity.ScheduleEntryEntity())
                : new com.dm.data.entity.ScheduleEntryEntity();

        if (dto.getOfferingId() != null) {
            entity.setOffering(offeringRepo.findById(dto.getOfferingId())
                    .orElseThrow(() -> new IllegalArgumentException("Offering not found")));
        }
        if (dto.getRoomId() != null) {
            entity.setRoom(roomRepo.findById(dto.getRoomId())
                    .orElseThrow(() -> new IllegalArgumentException("Room not found")));
        }
        if (dto.getTimeslotId() != null) {
            entity.setTimeslot(timeslotRepo.findById(dto.getTimeslotId())
                    .orElseThrow(() -> new IllegalArgumentException("Timeslot not found")));
        }

        entity.setWeekPattern(dto.getWeekPattern());
        entity.setStatus(dto.getStatus());
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<ScheduleEntryDto> getByTeacherEmail(String email) {
        var teacher = teacherRepo.findByUserEmail(email).orElse(null);
        if (teacher == null)
            return List.of();
        return getByTeacherId(teacher.getId());
    }

    public List<ScheduleEntryDto> getByGroupIds(List<Long> groupIds) {
        return repository.findAll().stream()
                .filter(e -> groupIds.contains(e.getOffering().getGroup().getId()))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteByGroupIds(List<Long> groupIds) {
        // Fetch entities first to ensure cascade or logic if needed, or delete directly
        var entities = repository.findAll().stream()
                .filter(e -> groupIds.contains(e.getOffering().getGroup().getId()))
                .collect(Collectors.toList());
        repository.deleteAll(entities);
    }
}