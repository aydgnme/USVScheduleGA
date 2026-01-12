package com.dm.dto;

import com.dm.model.types.ScheduleStatus;
import com.dm.model.types.WeekParity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for scheduled entries combining offering, timeslot, and room.
 */
public class ScheduleEntryDto implements Serializable {
    public ScheduleEntryDto() {
    }

    public ScheduleEntryDto(Long id, Long offeringId, Long timeslotId, Long roomId, WeekParity weekPattern,
            ScheduleStatus status, String courseCode, String courseTitle, String groupCode, String teacherFirstName,
            String teacherLastName, String roomCode, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.offeringId = offeringId;
        this.timeslotId = timeslotId;
        this.roomId = roomId;
        this.weekPattern = weekPattern;
        this.status = status;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.groupCode = groupCode;
        this.teacherFirstName = teacherFirstName;
        this.teacherLastName = teacherLastName;
        this.roomCode = roomCode;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    Long id;
    Long offeringId;
    Long timeslotId;
    Long roomId;
    WeekParity weekPattern;
    ScheduleStatus status;

    // View fields
    String courseCode;
    String courseTitle;
    String groupCode;
    String teacherFirstName;
    String teacherLastName;
    String roomCode;
    DayOfWeek dayOfWeek;
    LocalTime startTime;
    LocalTime endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOfferingId() {
        return offeringId;
    }

    public void setOfferingId(Long offeringId) {
        this.offeringId = offeringId;
    }

    public Long getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(Long timeslotId) {
        this.timeslotId = timeslotId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public WeekParity getWeekPattern() {
        return weekPattern;
    }

    public void setWeekPattern(WeekParity weekPattern) {
        this.weekPattern = weekPattern;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getTeacherFirstName() {
        return teacherFirstName;
    }

    public void setTeacherFirstName(String teacherFirstName) {
        this.teacherFirstName = teacherFirstName;
    }

    public String getTeacherLastName() {
        return teacherLastName;
    }

    public void setTeacherLastName(String teacherLastName) {
        this.teacherLastName = teacherLastName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
