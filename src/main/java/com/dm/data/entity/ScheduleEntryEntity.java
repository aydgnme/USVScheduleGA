package com.dm.data.entity;

import com.dm.model.types.ScheduleStatus;
import com.dm.model.types.WeekParity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schedule_entries", indexes = {
        @Index(name = "ix_se_offering", columnList = "offering_id"),
        @Index(name = "ix_se_timeslot", columnList = "timeslot_id"),
        @Index(name = "ix_se_room", columnList = "room_id"),
        @Index(name = "ix_se_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id", nullable = false)
    private CourseOfferingEntity offering;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot_id", nullable = false)
    private TimeslotEntity timeslot;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_pattern", nullable = false, length = 10)
    private WeekParity weekPattern;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 15)
    private ScheduleStatus status;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}
