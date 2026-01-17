package com.dm.qa;

import com.dm.data.entity.Role;
import com.dm.data.entity.TeacherProfileEntity;
import com.dm.data.entity.UserEntity;
import com.dm.data.repository.TeacherRepository;
import com.dm.data.repository.UserRepository;
import com.dm.dto.TeacherPreferenceDto;
import com.dm.model.types.PreferenceType;
import com.dm.service.TeacherAvailabilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TeacherFlowTest {

    @Autowired
    private TeacherAvailabilityService availabilityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void testTeacherAvailabilityFlow() {
        // 1. Setup Teacher User
        UserEntity user = new UserEntity();
        user.setEmail("qa.teacher." + System.currentTimeMillis() + "@usv.ro");
        user.setPassword("password");
        user.setRole(Role.TEACHER);
        user.setEnabled(true);
        user = userRepository.save(user);

        TeacherProfileEntity teacher = new TeacherProfileEntity();
        teacher.setUser(user);
        teacher.setFirstName("QA");
        teacher.setLastName("Teacher");
        teacher.setMaxHoursWeekly(14);
        teacher = teacherRepository.save(teacher);

        Long teacherId = teacher.getId();
        assertNotNull(teacherId);

        // 2. Initial State - Empty Preferences
        List<TeacherPreferenceDto> initialPrefs = availabilityService.getPreferences(teacherId);
        assertTrue(initialPrefs.isEmpty(), "Should start with no preferences");

        // 3. Create a Preference (Monday 8-9 Preferred)
        TeacherPreferenceDto pref1 = new TeacherPreferenceDto();
        pref1.setTeacherId(teacherId);
        pref1.setDayOfWeek(DayOfWeek.MONDAY);
        pref1.setStartHour(8);
        pref1.setEndHour(9);
        pref1.setType(PreferenceType.PREFERRED);

        TeacherPreferenceDto saved1 = availabilityService.savePreference(pref1);
        assertNotNull(saved1.getId());
        assertEquals(PreferenceType.PREFERRED, saved1.getType());

        // 4. Create another Preference (Tuesday 10-11 Unavailable)
        TeacherPreferenceDto pref2 = new TeacherPreferenceDto();
        pref2.setTeacherId(teacherId);
        pref2.setDayOfWeek(DayOfWeek.TUESDAY);
        pref2.setStartHour(10);
        pref2.setEndHour(11);
        pref2.setType(PreferenceType.UNAVAILABLE);

        TeacherPreferenceDto saved2 = availabilityService.savePreference(pref2);
        assertNotNull(saved2.getId());

        // 5. Retrieve and Verify
        List<TeacherPreferenceDto> currentPrefs = availabilityService.getPreferences(teacherId);
        assertEquals(2, currentPrefs.size());

        boolean foundMonday = currentPrefs.stream()
                .anyMatch(p -> p.getDayOfWeek() == DayOfWeek.MONDAY && p.getType() == PreferenceType.PREFERRED);
        boolean foundTuesday = currentPrefs.stream()
                .anyMatch(p -> p.getDayOfWeek() == DayOfWeek.TUESDAY && p.getType() == PreferenceType.UNAVAILABLE);

        assertTrue(foundMonday, "Monday preference not found");
        assertTrue(foundTuesday, "Tuesday preference not found");

        // 6. Update Preference (Monday 8-9 -> Unavailable)
        saved1.setType(PreferenceType.UNAVAILABLE);
        TeacherPreferenceDto updated1 = availabilityService.savePreference(saved1);
        assertEquals(PreferenceType.UNAVAILABLE, updated1.getType());

        // 7. Verify Update
        currentPrefs = availabilityService.getPreferences(teacherId);
        TeacherPreferenceDto mondayPref = currentPrefs.stream()
                .filter(p -> p.getDayOfWeek() == DayOfWeek.MONDAY)
                .findFirst()
                .orElseThrow();
        assertEquals(PreferenceType.UNAVAILABLE, mondayPref.getType());

        // 8. Delete Preference
        availabilityService.deletePreference(saved2.getId());

        // 9. Verify Deletion
        currentPrefs = availabilityService.getPreferences(teacherId);
        assertEquals(1, currentPrefs.size());
        assertFalse(currentPrefs.stream().anyMatch(p -> p.getDayOfWeek() == DayOfWeek.TUESDAY));
    }
}
