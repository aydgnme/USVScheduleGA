package com.dm.mapper;

import com.dm.dto.TimeslotDto;
import com.dm.data.entity.Timeslot;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between {@link Timeslot} entity and {@link TimeslotDto}.
 */
@Component
public class TimeslotMapper {

    /**
     * Converts a Timeslot entity to a TimeslotDto.
     * @param entity The Timeslot entity.
     * @return The corresponding TimeslotDto.
     */
    public TimeslotDto toDto(Timeslot entity) {
        if (entity == null) {
            return null;
        }
        return new TimeslotDto(
                entity.getId(),
                entity.getDay(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }

    /**
     * Converts a TimeslotDto to a Timeslot entity.
     * @param dto The TimeslotDto.
     * @return The corresponding Timeslot entity.
     */
    public Timeslot toEntity(TimeslotDto dto) {
        if (dto == null) {
            return null;
        }
        Timeslot t = new Timeslot();
        t.setId(dto.getId());
        t.setDay(dto.getDay());
        t.setStartTime(dto.getStartTime());
        t.setEndTime(dto.getEndTime());
        return t;
    }
}