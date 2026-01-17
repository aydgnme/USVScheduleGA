package com.dm.mapper;

import com.dm.data.entity.CourseOfferingEntity;
import com.dm.dto.CourseOfferingDto;
import com.dm.model.CourseOffering;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

/**
 * Mapper for CourseOfferingEntity ⇄ CourseOfferingDto and CourseOfferingEntity ⇄ CourseOffering domain.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CourseMapper.class, GroupMapper.class, TeacherMapper.class})
public interface CourseOfferingMapper {

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.code", target = "courseCode")
    @Mapping(source = "course.title", target = "courseTitle")
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.code", target = "groupCode")
    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "teacher.firstName", target = "teacherFirstName")
    @Mapping(source = "teacher.lastName", target = "teacherLastName")
    CourseOfferingDto toDto(CourseOfferingEntity entity);

    @Mapping(source = "course", target = "course")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "teacher", target = "teacher")
    CourseOffering toDomain(CourseOfferingEntity entity);

    @Mapping(target = "course", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    CourseOfferingEntity toEntity(CourseOfferingDto dto);

    CourseOfferingEntity toEntityFromDomain(CourseOffering domain);
}
