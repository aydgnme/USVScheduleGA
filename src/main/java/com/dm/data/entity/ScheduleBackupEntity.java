package com.dm.data.entity;

import com.dm.model.types.ScheduleStatus;
import com.dm.model.types.WeekParity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_backups", indexes = {
        @Index(name = "ix_sb_original_id", columnList = "original_entry_id"),
        @Index(name = "ix_sb_group", columnList = "group_id"),
        @Index(name = "ix_sb_backup_ts", columnList = "backup_timestamp")
})
@Data
@NoArgsConstructor
public class ScheduleBackupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_entry_id")
    private Long originalEntryId;

    // We store simplified data or references.
    // Ideally, we keep references if we want to query by them,
    // but to avoid issues if referenced entities are deleted, we might validly
    // store IDs or names.
    // However, given the app structure, entities (CourseOffering, Room, Timeslot)
    // are stable.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id")
    private CourseOfferingEntity offering;

    // For easier querying by group without joining offering
    @Column(name = "group_id")
    private Long groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot_id")
    private TimeslotEntity timeslot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_pattern", length = 10)
    private WeekParity weekPattern;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 15)
    private ScheduleStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "backup_timestamp", nullable = false)
    private LocalDateTime backupTimestamp;

    public ScheduleBackupEntity(ScheduleEntryEntity original) {
        this.originalEntryId = original.getId();
        this.offering = original.getOffering();
        this.groupId = original.getOffering() != null ? original.getOffering().getGroup().getId() : null;
        this.timeslot = original.getTimeslot();
        this.room = original.getRoom();
        this.weekPattern = original.getWeekPattern();
        this.status = original.getStatus();
        this.createdAt = original.getCreatedAt();
        this.backupTimestamp = LocalDateTime.now();
    }
}
