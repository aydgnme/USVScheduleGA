package com.dm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * Teacher profile DTO (auth data via userId).
 */
public class TeacherDto implements Serializable {
    public TeacherDto() {
    }

    public TeacherDto(Long id, Long userId, String firstName, String lastName, String email, Integer maxHoursWeekly,
            Set<DepartmentDto> departments, String availableDaysJson, String preferredTime, String note) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.maxHoursWeekly = maxHoursWeekly;
        this.departments = departments;
        this.availableDaysJson = availableDaysJson;
        this.preferredTime = preferredTime;
        this.note = note;
    }

    Long id;
    Long userId;
    String firstName;
    String lastName;
    String email;
    Integer maxHoursWeekly;
    Set<DepartmentDto> departments;
    String availableDaysJson;
    String preferredTime;
    String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMaxHoursWeekly() {
        return maxHoursWeekly;
    }

    public void setMaxHoursWeekly(Integer maxHoursWeekly) {
        this.maxHoursWeekly = maxHoursWeekly;
    }

    public Set<DepartmentDto> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<DepartmentDto> departments) {
        this.departments = departments;
    }

    public String getAvailableDaysJson() {
        return availableDaysJson;
    }

    public void setAvailableDaysJson(String availableDaysJson) {
        this.availableDaysJson = availableDaysJson;
    }

    public String getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(String preferredTime) {
        this.preferredTime = preferredTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}