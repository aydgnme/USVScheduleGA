package com.dm.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.dm.model.Teacher}
 */
public class TeacherDto implements Serializable {

    private Long id;
    private String name;
    private String email;
    private Integer maxHoursWeekly;
    private String department;
    private String availableDays;
    private String preferredTime;

    public TeacherDto() {
    }

    public TeacherDto(Long id,
                      String name,
                      String email,
                      Integer maxHoursWeekly,
                      String department,
                      String availableDays,
                      String preferredTime) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.maxHoursWeekly = maxHoursWeekly;
        this.department = department;
        this.availableDays = availableDays;
        this.preferredTime = preferredTime;
    }

    // getters & setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getMaxHoursWeekly() { return maxHoursWeekly; }
    public void setMaxHoursWeekly(Integer maxHoursWeekly) { this.maxHoursWeekly = maxHoursWeekly; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getAvailableDays() { return availableDays; }
    public void setAvailableDays(String availableDays) { this.availableDays = availableDays; }

    public String getPreferredTime() { return preferredTime; }
    public void setPreferredTime(String preferredTime) { this.preferredTime = preferredTime; }
}