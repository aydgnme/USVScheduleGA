package com.dm.mapper;

import com.dm.data.entity.ScheduleEntryEntity;
import com.dm.dto.ScheduleEntryDto;
import com.dm.model.ScheduleEntry;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

/**
 * Mapper for ScheduleEntryEntity ⇄ ScheduleEntryDto and ScheduleEntryEntity ⇄
 * ScheduleEntry domain.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { CourseOfferingMapper.class,
        TimeslotMapper.class, RoomMapper.class })
public interface ScheduleEntryMapper {

    @Mapping(source = "offering.id", target = "offeringId")
    @Mapping(source = "offering.course.code", target = "courseCode")
    @Mapping(source = "offering.course.title", target = "courseTitle")
    @Mapping(source = "offering.group.code", target = "groupCode")
    @Mapping(source = "offering.teacher.firstName", target = "teacherFirstName")
    @Mapping(source = "offering.teacher.lastName", target = "teacherLastName")
    @Mapping(source = "timeslot.id", target = "timeslotId")
    @Mapping(source = "timeslot.dayOfWeek", target = "dayOfWeek")
    @Mapping(source = "timeslot.startTime", target = "startTime")
    @Mapping(source = "timeslot.endTime", target = "endTime")
    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.code", target = "roomCode")
    ScheduleEntryDto toDto(ScheduleEntryEntity entity);

    @Mapping(source = "offering", target = "offering")
    @Mapping(source = "timeslot", target = "timeslot")
    @Mapping(source = "room", target = "room")
    ScheduleEntry toDomain(ScheduleEntryEntity entity);

    @Mapping(target = "offering", ignore = true)
    @Mapping(target = "timeslot", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ScheduleEntryEntity toEntity(ScheduleEntryDto dto);

    @Mapping(target = "createdAt", ignore = true)
    ScheduleEntryEntity toEntityFromDomain(ScheduleEntry domain);
}
