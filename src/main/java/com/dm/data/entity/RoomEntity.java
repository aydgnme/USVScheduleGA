package com.dm.data.entity;

import com.dm.model.types.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms", indexes = {
        @Index(name = "uk_rooms_code", columnList = "code", unique = true),
        @Index(name = "ix_rooms_building", columnList = "building")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code; // e.g., C001, A115, ONLINE

    @Column(name = "capacity")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", length = 20)
    private RoomType roomType; // LECTURE_HALL, LAB, SEMINAR_ROOM, MEETING, VIRTUAL, OTHER

    @Column(name = "building", length = 20)
    private String building; // C, A, D, E, B, J, VIRTUAL

    @Column(name = "computers")
    private Integer computers;

    @Lob
    @Column(name = "features_json", columnDefinition = "TEXT")
    private String featuresJson;
}
