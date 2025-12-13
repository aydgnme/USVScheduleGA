package com.dm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    private Long id;
    private String name;
    private int studyYear;
    private String specialization;
    private String faculty;
}
