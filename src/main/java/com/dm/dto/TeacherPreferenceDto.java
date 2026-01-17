package com.dm.dto;

import com.dm.model.types.PreferenceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherPreferenceDto {
    private Long id;
    private Long teacherId;
    private DayOfWeek dayOfWeek;
    private Integer startHour;
    private Integer endHour;
    private PreferenceType type;
    private Integer priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public java.time.DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(java.time.DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public PreferenceType getType() {
        return type;
    }

    public void setType(PreferenceType type) {
        this.type = type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
