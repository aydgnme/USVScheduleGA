package com.dm.mapper;

import com.dm.dto.TimeslotDto;
import com.dm.model.Timeslot;
import org.springframework.stereotype.Component;

@Component
public class TimeslotMapper {

    public TimeslotDto toDto(Timeslot entity) {
        return new TimeslotDto(
                entity.getId(),
                entity.getDay(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }

    public Timeslot toEntity(TimeslotDto dto) {
        Timeslot t = new Timeslot();
        t.setId(dto.getId());
        t.setDay(dto.getDay());
        t.setStartTime(dto.getStartTime());
        t.setEndTime(dto.getEndTime());
        return t;
    }
}