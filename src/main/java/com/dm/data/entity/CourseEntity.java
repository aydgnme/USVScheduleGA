package com.dm.data.entity;

import com.dm.model.types.CourseComponentType;
import com.dm.model.types.WeekParity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses", indexes = {
        @Index(name = "uk_courses_code", columnList = "code", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseComponentType componentType;

    @Column(nullable = false)
    private int credits;

    @Column(nullable = false)
    private int semester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private WeekParity parity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CourseComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(CourseComponentType componentType) {
        this.componentType = componentType;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public WeekParity getParity() {
        return parity;
    }

    public void setParity(WeekParity parity) {
        this.parity = parity;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }
}
