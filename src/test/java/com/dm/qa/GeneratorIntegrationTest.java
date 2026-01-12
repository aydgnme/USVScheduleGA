package com.dm.qa;

import com.dm.algorithm.service.GeneticAlgorithmService;
import com.dm.algorithm.model.Chromosome;
import com.dm.data.entity.*;
import com.dm.data.repository.*;
import com.dm.dto.CourseOfferingDto;
import com.dm.model.types.CourseComponentType;
import com.dm.model.types.WeekParity;
import com.dm.service.CourseOfferingService;
import com.dm.service.ScheduleService;
import com.dm.service.TimeslotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GeneratorIntegrationTest {

    @Autowired
    private GeneticAlgorithmService gaService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TimeslotRepository timeslotRepository;
    @Autowired
    private CourseOfferingService offeringService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private com.dm.data.repository.SpecializationRepository specializationRepository;

    @Test
    public void testFullGenerationLoop() {
        // 1. Setup Infrastructure (Faculty, Dept)
        // Use existing FIESC / CALC if available (due to data seeds) or create if empty
        FacultyEntity faculty = facultyRepository.findByCode("FIESC").orElseGet(() -> {
            FacultyEntity f = new FacultyEntity();
            f.setCode("FIESC");
            f.setName("Facultatea de Inginerie Electrică");
            f.setShortName("FIESC");
            return facultyRepository.save(f);
        });

        DepartmentEntity dept = departmentRepository.findByCode("CALC").orElseGet(() -> {
            DepartmentEntity d = new DepartmentEntity();
            d.setCode("CALC");
            d.setName("Calculatoare");
            d.setFaculty(faculty);
            return departmentRepository.save(d);
        });

        // 2. Setup Resources (Teacher, Room, Group)
        UserEntity user = new UserEntity();
        user.setEmail("gen.teacher@test.com");
        user.setPassword("pass");
        user.setRole(Role.TEACHER);
        user.setEnabled(true);
        user = userRepository.save(user);

        TeacherProfileEntity teacher = new TeacherProfileEntity();
        teacher.setUser(user);
        teacher.setFirstName("Gen");
        teacher.setLastName("Teacher");
        teacher.getDepartments().add(dept);
        teacher = teacherRepository.save(teacher);

        RoomEntity room = new RoomEntity();
        room.setCode("R101");
        // room.setName("Room 101"); // Field does not exist
        // room.setDepartment(dept); // Field does not exist
        room.setBuilding("C");
        room.setCapacity(30);
        room.setRoomType(com.dm.model.types.RoomType.LECTURE_HALL);
        room = roomRepository.save(room);

        // ...

        SpecializationEntity spec = new SpecializationEntity();
        spec.setCode("SPEC_TEST");
        spec.setName("Test Spec");
        spec.setDepartment(dept);
        spec.setStudyCycle(com.dm.model.types.StudyCycle.BACHELOR.name());
        spec = specializationRepository.save(spec);

        GroupEntity group = new GroupEntity();
        group.setCode("G1");
        group.setStudyYear(1);
        group.setGroupNumber(1);
        group.setSpecialization(spec);
        group = groupRepository.save(group);

        // 3. Setup Timeslots (Mon 8-10, Mon 10-12)
        TimeslotEntity ts1 = new TimeslotEntity();
        ts1.setDayOfWeek(DayOfWeek.MONDAY);
        ts1.setStartTime(LocalTime.of(8, 0));
        ts1.setEndTime(LocalTime.of(10, 0));
        timeslotRepository.save(ts1);

        TimeslotEntity ts2 = new TimeslotEntity();
        ts2.setDayOfWeek(DayOfWeek.MONDAY);
        ts2.setStartTime(LocalTime.of(10, 0));
        ts2.setEndTime(LocalTime.of(12, 0));
        timeslotRepository.save(ts2);

        // 4. Setup Course & Offering
        CourseEntity course = new CourseEntity();
        course.setCode("CS101");
        course.setTitle("Intro to Gen");
        course.setCredits(5);
        course.setSemester(1);
        course.setDepartment(dept);
        course.setComponentType(CourseComponentType.LECTURE);
        course.setParity(WeekParity.BOTH);
        course = courseRepository.save(course);

        CourseOfferingDto offering = new CourseOfferingDto();
        offering.setCourseId(course.getId());
        offering.setTeacherId(teacher.getId());
        offering.setGroupId(group.getId());
        offering.setWeeklyHours(2); // Should map to 1 gene/timeslot
        offering.setParity(WeekParity.BOTH);
        CourseOfferingDto savedOffering = offeringService.save(offering);

        assertNotNull(savedOffering.getId());

// 5. Run Generation
// Set small pop/gen for speed
        gaService.setPopulationSize(10);
        gaService.setMutationRate(0.1);
        gaService.setElitismCount(1);

// CHANGE THIS LINE: Change type to GenerationResult (or use var)
        var result = gaService.runFullGeneration(5, java.util.List.of(group.getId()));

        assertNotNull(result);
// CHANGE THIS LINE: Use result.entries() instead of best.getGenes()
        assertTrue(result.entries().size() > 0, "Generated schedule should have entries");

// 6. Verify Persistence
        var scheduledEntries = scheduleService.getAll();
        assertFalse(scheduledEntries.isEmpty(), "Schedule should be saved to DB");

// Find our specific entry
        boolean found = scheduledEntries.stream()
                .anyMatch(e -> e.getOfferingId().equals(savedOffering.getId()));
        assertTrue(found, "Our offering should be scheduled");
    }
}