package com.dm.data.repository;

import com.dm.data.entity.RoomEntity;
import com.dm.model.types.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for RoomEntity.
 */
@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Optional<RoomEntity> findByCode(String code);

    boolean existsByCode(String code);

    List<RoomEntity> findAllByCodeIn(Iterable<String> codes);

    List<RoomEntity> findAllByRoomType(RoomType roomType);

    List<RoomEntity> findAllByCapacityGreaterThanEqual(int minCapacity);

    List<RoomEntity> findAllByBuilding(String building);

    List<RoomEntity> findAllByBuildingAndComputersGreaterThan(String building, int minComputers);
}

