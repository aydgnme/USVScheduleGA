package com.dm.service;

import com.dm.data.entity.*;
import com.dm.data.repository.*;
import com.dm.dto.CourseOfferingDto;
import com.dm.model.types.WeekParity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ServiceLayerTest {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SpecializationService specializationService;

    @Autowired
    private CourseOfferingService courseOfferingService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        // Setup data will be transactional and rolled back
    }

    @Test
    public void testDepartmentServiceFetchFaculty() {
        // Reuse existing or create if not exists
        FacultyEntity faculty = facultyRepository.findByCode("FIESC")
                .orElseGet(() -> {
                    FacultyEntity f = new FacultyEntity();
                    f.setCode("FIESC");
                    f.setName("Test Faculty");
                    return facultyRepository.save(f);
                });

        DepartmentEntity dept = new DepartmentEntity();
        dept.setCode("TDEPT_" + System.currentTimeMillis());
        dept.setName("Test Department");
        dept.setFaculty(faculty);
        departmentRepository.save(dept);

        // Test Service Method
        List<DepartmentEntity> departments = departmentService.getAll();
        assertFalse(departments.isEmpty());

        DepartmentEntity fetchedDept = departments.stream()
                .filter(d -> dept.getCode().equals(d.getCode()))
                .findFirst()
                .orElse(null);

        assertNotNull(fetchedDept);
        assertNotNull(fetchedDept.getFaculty());
        assertEquals("FIESC", fetchedDept.getFaculty().getCode());
    }

    @Test
    public void testSpecializationServiceFetchDepartment() {
        FacultyEntity faculty = facultyRepository.findByCode("FIM")
                .orElseGet(() -> {
                    FacultyEntity f = new FacultyEntity();
                    f.setCode("FIM");
                    f.setName("Spec Faculty");
                    return facultyRepository.save(f);
                });

        DepartmentEntity dept = new DepartmentEntity();
        dept.setCode("SDEPT_" + System.currentTimeMillis());
        dept.setName("Spec Department");
        dept.setFaculty(faculty);
        dept = departmentRepository.save(dept);

        SpecializationEntity spec = new SpecializationEntity();
        spec.setCode("TSPEC_" + System.currentTimeMillis());
        spec.setName("Test Specialization");
        spec.setDepartment(dept);
        spec.setStudyCycle("BACHELOR");
        specializationRepository.save(spec);

        // Test Service Method
        List<SpecializationEntity> specs = specializationService.getAll();
        assertFalse(specs.isEmpty());

        SpecializationEntity fetchedSpec = specs.stream()
                .filter(s -> spec.getCode().equals(s.getCode()))
                .findFirst()
                .orElse(null);

        assertNotNull(fetchedSpec);
        assertNotNull(fetchedSpec.getDepartment());
        assertEquals("Spec Department", fetchedSpec.getDepartment().getName());
    }

    @Test
    public void testCourseOfferingServiceFetchDetails() {
        // 1. Create User & Teacher
        UserEntity user = new UserEntity();
        user.setEmail("test.teacher." + System.currentTimeMillis() + "@test.com");
        user.setPassword("pass");
        user.setRole(com.dm.data.entity.Role.TEACHER);
        user.setEnabled(true);
        user = userRepository.save(user);

        FacultyEntity faculty = facultyRepository.findByCode("FEFS")
                .orElseGet(() -> {
                    FacultyEntity f = new FacultyEntity();
                    f.setCode("FEFS");
                    f.setName("Offering Faculty");
                    return facultyRepository.save(f);
                });

        DepartmentEntity dept = new DepartmentEntity();
        dept.setCode("ODEPT_" + System.currentTimeMillis());
        dept.setName("Offering Dept");
        dept.setFaculty(faculty);
        dept = departmentRepository.save(dept);

        TeacherProfileEntity teacher = new TeacherProfileEntity();
        teacher.setUser(user);
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.getDepartments().add(dept);
        teacher.setMaxHoursWeekly(10);
        teacher = teacherRepository.save(teacher);

        // 2. Create Course
        CourseEntity course = new CourseEntity();
        course.setCode("TCOURSE_" + System.currentTimeMillis());
        course.setTitle("Test Course");
        course.setCredits(5);
        course.setSemester(1);
        course.setDepartment(dept);
        course.setComponentType(com.dm.model.types.CourseComponentType.LECTURE);
        course.setParity(WeekParity.BOTH);
        course = courseRepository.save(course);

        // 3. Create Specialization & Group
        SpecializationEntity spec = new SpecializationEntity();
        spec.setCode("OSPEC_" + System.currentTimeMillis());
        spec.setName("Offering Spec");
        spec.setDepartment(dept);
        spec.setStudyCycle("BACHELOR");
        spec = specializationRepository.save(spec);

        GroupEntity group = new GroupEntity();
        group.setCode("TGROUP_" + System.currentTimeMillis());
        group.setSpecialization(spec);
        group.setStudyYear(1);
        group.setGroupNumber(1);
        group = groupRepository.save(group);

        // 4. Create Offering via DTO to test Service.save as well
        CourseOfferingDto dto = new CourseOfferingDto();
        dto.setCourseId(course.getId());
        dto.setGroupId(group.getId());
        dto.setTeacherId(teacher.getId());
        dto.setWeeklyHours(2);
        dto.setParity(WeekParity.BOTH);

        // This implicitly calls findAllWithDetails inside getAll later?
        // Let's call save first
        CourseOfferingDto saved = courseOfferingService.save(dto);
        assertNotNull(saved.getId());

        // 5. Test getAll() for eager fetching
        List<CourseOfferingDto> allOfferings = courseOfferingService.getAll();
        assertFalse(allOfferings.isEmpty());

        CourseOfferingDto fetchedDto = allOfferings.stream()
                .filter(o -> saved.getId().equals(o.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(fetchedDto);
        // In DTO mapping, if relations weren't fetched, accessing properties might fail
        // or be null if mapped from entity
        assertEquals("Test Course", fetchedDto.getCourseTitle());
        assertEquals(group.getCode(), fetchedDto.getGroupCode());
        assertEquals("Test", fetchedDto.getTeacherFirstName());
    }
}
