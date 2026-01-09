package com.dm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    private Long id;
    private String code;                 // e.g. LIC1A
    private Long specializationId;
    private Integer studyYear;           // 0-4
    private Integer groupNumber;
    private String subgroupIndex;        // a, b, c
    private Integer isModular;           // 0 or 1
    private Specialization specialization;
}
