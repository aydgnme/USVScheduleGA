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
}
