package com.dm.dto;

import com.dm.model.types.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * Room DTO with type, building, and feature list.
 */
public class RoomDto implements Serializable {
    public RoomDto() {
    }

    public RoomDto(Long id, String code, Integer capacity, RoomType roomType, String building, Integer computers,
            Set<String> features) {
        this.id = id;
        this.code = code;
        this.capacity = capacity;
        this.roomType = roomType;
        this.building = building;
        this.computers = computers;
        this.features = features;
    }

    Long id;
    String code;
    Integer capacity;
    RoomType roomType;
    String building;
    Integer computers;
    Set<String> features;

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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Integer getComputers() {
        return computers;
    }

    public void setComputers(Integer computers) {
        this.computers = computers;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }
}