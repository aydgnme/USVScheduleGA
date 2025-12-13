package com.dm.data.repository;

import com.dm.data.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByCode(String code);
    List<Room> findAllByType(String type);
    List<Room> findAllByCapacityGreaterThanEqual(int minCapacity);
}
