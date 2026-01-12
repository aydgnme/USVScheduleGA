package com.dm.qa;

import com.dm.algorithm.service.GeneticAlgorithmService;
import com.dm.data.entity.*;
import com.dm.data.repository.*;
import com.dm.model.types.ScheduleStatus;
import com.dm.model.types.WeekParity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@Transactional
public class BackupIntegrationTest {

    @Autowired
    private GeneticAlgorithmService geneticAlgorithmService;

    @Autowired
    private ScheduleEntryRepository scheduleEntryRepository;

    @Autowired
    private ScheduleBackupRepository scheduleBackupRepository;

    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void testBackupMechanism() {
        // 1. Setup: Create a schedule entry for a group
        // We find an existing offering, timeslot, and room to avoid complex setup if
        // possible,
        // or just create a dummy one if the DB is empty (integration tests usually have
        // data or rely on H2).
        // Let's rely on finding one, or fail if empty.

        List<CourseOfferingEntity> offerings = courseOfferingRepository.findAll();
        if (offerings.isEmpty())
            return; // Cannot test without data

        CourseOfferingEntity offering = offerings.get(0);
        Long groupId = offering.getGroup().getId();

        TimeslotEntity timeslot = timeslotRepository.findAll().get(0);
        RoomEntity room = roomRepository.findAll().get(0);

        ScheduleEntryEntity entry = new ScheduleEntryEntity();
        entry.setOffering(offering);
        entry.setTimeslot(timeslot);
        entry.setRoom(room);
        entry.setWeekPattern(WeekParity.BOTH);
        entry.setStatus(ScheduleStatus.PLANNED);
        entry.setCreatedAt(LocalDateTime.now());

        entry = scheduleEntryRepository.save(entry);
        Long originalId = entry.getId();

        // Verify it exists in main table
        Assertions.assertTrue(scheduleEntryRepository.findById(originalId).isPresent());

        // 2. Action: Trigger backupAndClear via runFullGeneration (or we can't call
        // private method,
        // but runFullGeneration calls it).
        // runFullGeneration generates new solutions. We just want to see the backup
        // happen.
        // 2. Action: Trigger backup directly
        geneticAlgorithmService.backupScheduleForGroups(Collections.singletonList(groupId));

        // 3. Assert:
        // - Original entry should be GONE (or at least different if overwritten, but we
        // expect delete/insert)
        // - Loop in backupAndClear deletes entries.
        // - New entries might be created.

        // Check backup table
        List<ScheduleBackupEntity> backups = scheduleBackupRepository.findByGroupId(groupId);
        Assertions.assertFalse(backups.isEmpty(), "Backup table should not be empty");

        boolean foundOriginal = backups.stream().anyMatch(b -> b.getOriginalEntryId().equals(originalId));
        Assertions.assertTrue(foundOriginal, "Original entry should be found in backup");

        // Check main table does NOT contain the original entity ID (it might have new
        // ones)
        // Note: Delete uses ID.
        Assertions.assertFalse(scheduleEntryRepository.findById(originalId).isPresent(),
                "Original entry should be removed from main table");
    }
}
