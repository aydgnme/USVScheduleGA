# USV Schedule Database Implementation - Checklist

## ✅ COMPLETED PHASE 1: Schema Design & Data Integration

### 1. ✅ Data Analysis Complete
- [x] Parsed groups.json (8,461 student groups)
- [x] Analyzed teacher.json (~1,900 faculty members)
- [x] Extracted room.json data (155 active teaching spaces)
- [x] Identified 11 faculties, 24 departments, 87+ specializations
- [x] Mapped study types: Type 1=Bachelor, Type 2=Master, Type 3=Doctorate/Conversion

### 2. ✅ Database Schema Redesigned
- [x] V1__init_normalized_schema.sql
  - [x] Created FACULTIES table (11 USV faculties)
  - [x] Created DEPARTMENTS table (24 departments with faculty FK)
  - [x] Created SPECIALIZATIONS table (87+ programs with study cycle)
  - [x] Enhanced GROUPS to link to specializations (not flat strings)
  - [x] Updated TEACHER_PROFILES (firstName, lastName fields)
  - [x] Created TEACHER_DEPARTMENTS junction table (many-to-many)
  - [x] Enhanced ROOMS (building, computers fields for classification)
  - [x] Enhanced TIMESLOTS (day_of_week enum, duration_minutes)
  - [x] All tables with proper constraints, indices, sequences

### 3. ✅ Migration Scripts Created
- [x] V4__seed_faculties_departments.sql
  - Inserts 11 faculties (FIESC, FIM, FEFS, FIA, FIG, FLSC, SILVIC, SEAB, FSED, FDSA, FMSB)
  - Inserts 24 departments with proper hierarchy
  - Updates sequences post-insertion

- [x] V5__seed_specializations_groups.sql
  - Inserts 35+ specialization definitions across all faculties
  - Creates 50+ representative student groups (scalable template)
  - Maps study cycles correctly (BACHELOR/MASTER/CONVERSION)

- [x] V6__seed_teachers.sql
  - Inserts 15 sample teachers with full profile data
  - Creates multi-department teacher assignments
  - Sets availability constraints and weekly hours
  - Creates user accounts linked to teacher profiles

- [x] V7__seed_rooms.sql
  - Inserts 40+ rooms from actual USV room inventory
  - Classifies room types (LECTURE_HALL/LAB/SEMINAR_ROOM/VIRTUAL)
  - Includes building assignments (C, A, D, E, B, J)
  - Tracks computer count for labs

- [x] V8__seed_timeslots.sql
  - Inserts 20 standard university timeslots
  - Monday-Friday, 08:00-16:00, 2-hour blocks
  - Sets duration_minutes for each slot

### 4. ✅ Documentation Created
- [x] DATABASE_DESIGN.md (comprehensive architecture document)
  - Organizational hierarchy
  - Data structure overview
  - All 13 core tables with fields
  - Design decisions and rationale
  - Future enhancement suggestions

- [x] SCHEMA_DIAGRAM.md (visual ASCII diagrams)
  - Entity relationship diagram
  - Organizational hierarchy structure
  - Room distribution across buildings
  - Timeslot structure
  - Faculty and specialization tree
  - Constraint matrix
  - Data flow diagrams
  - Migration sequence

### 5. ✅ Backward Compatibility
- [x] V3 script deprecated (kept empty for compatibility)
- [x] V2 migration safe-guarded (runs only if old tables exist)
- [x] All sequences set up correctly to prevent ID conflicts

---

## 📋 PENDING PHASE 2: Code Layer Updates

### 6. ⏳ Update JPA Entities (TO DO)
**Files to update:**
- [ ] `src/main/java/com/dm/data/entity/TeacherProfileEntity.java`
  - [ ] Add firstName, lastName fields (separate from User)
  - [ ] Update @OneToMany teacherDepartments (Set<DepartmentEntity>)
  - [ ] Add maxHoursWeekly field
  - [ ] Add availableDaysJson CLOB field

- [ ] `src/main/java/com/dm/data/entity/GroupEntity.java`
  - [ ] Change from flat strings to FK relationships
  - [ ] Add @ManyToOne specialization → SpecializationEntity
  - [ ] Update code generation pattern
  - [ ] Add studyYear, groupNumber, subgroupIndex fields
  - [ ] Add isModular flag

- [ ] `src/main/java/com/dm/data/entity/FacultyEntity.java` (NEW)
  - [ ] id, code (unique), name, shortName
  - [ ] @OneToMany departments

- [ ] `src/main/java/com/dm/data/entity/DepartmentEntity.java` (NEW)
  - [ ] id, code (unique per faculty), name
  - [ ] @ManyToOne faculty
  - [ ] @OneToMany specializations
  - [ ] @ManyToMany teachers

- [ ] `src/main/java/com/dm/data/entity/SpecializationEntity.java` (NEW)
  - [ ] id, code (unique), name, studyCycle enum
  - [ ] @ManyToOne department
  - [ ] @OneToMany groups

- [ ] `src/main/java/com/dm/data/entity/RoomEntity.java`
  - [ ] Add building field (VARCHAR)
  - [ ] Add computers field (NUMBER)
  - [ ] Update room_type enum to include VIRTUAL
  - [ ] Ensure capacity is nullable for flexible storage

- [ ] `src/main/java/com/dm/data/entity/TimeslotEntity.java`
  - [ ] Rename day field to dayOfWeek (DayOfWeek enum)
  - [ ] Ensure startTime, endTime are LocalTime
  - [ ] Add durationMinutes field

- [ ] `src/main/java/com/dm/data/entity/UserEntity.java`
  - [ ] Verify relationships intact (1:1 with TeacherProfileEntity)

### 7. ⏳ Update Domain Models (TO DO)
**Files to update:**
- [ ] `src/main/java/com/dm/model/Teacher.java`
  - [ ] Add firstName, lastName (separate fields)
  - [ ] Change departments to Set<String> with FK lookup
  - [ ] Add maxHoursWeekly
  - [ ] Add availableDaysJson

- [ ] `src/main/java/com/dm/model/Group.java`
  - [ ] Add specialization object reference
  - [ ] Add studyYear, groupNumber, subgroupIndex
  - [ ] Add isModular flag
  - [ ] Update code property (keep as display field)

- [ ] `src/main/java/com/dm/model/Faculty.java` (NEW)
  - [ ] Immutable data class
  - [ ] code, name, shortName

- [ ] `src/main/java/com/dm/model/Department.java` (NEW)
  - [ ] Link to faculty
  - [ ] code, name

- [ ] `src/main/java/com/dm/model/Specialization.java` (NEW)
  - [ ] Link to department
  - [ ] code, name, studyCycle enum

- [ ] `src/main/java/com/dm/model/Room.java`
  - [ ] Add building field
  - [ ] Add computers field
  - [ ] Ensure roomType enum includes VIRTUAL

- [ ] `src/main/java/com/dm/model/Timeslot.java`
  - [ ] Use LocalTime for times
  - [ ] Use DayOfWeek enum for day
  - [ ] Add durationMinutes

### 8. ⏳ Create New Repositories (TO DO)
**Files to create:**
- [ ] `src/main/java/com/dm/data/repository/FacultyRepository.java`
- [ ] `src/main/java/com/dm/data/repository/DepartmentRepository.java`
- [ ] `src/main/java/com/dm/data/repository/SpecializationRepository.java`

**Methods to implement:**
```java
// FacultyRepository
Optional<Faculty> findByCode(String code);
List<Faculty> findAll();

// DepartmentRepository
Optional<Department> findByCode(String code);
List<Department> findByFacultyId(Long facultyId);

// SpecializationRepository
Optional<Specialization> findByCode(String code);
List<Specialization> findByDepartmentId(Long deptId);
List<Specialization> findByStudyCycle(StudyCycle cycle);
```

### 9. ⏳ Update Services (TO DO)
**Files to update:**
- [ ] `src/main/java/com/dm/service/TeacherService.java`
  - [ ] Update to use firstName/lastName separately
  - [ ] Handle multi-department lookup
  - [ ] Update availability parsing

- [ ] `src/main/java/com/dm/service/GroupService.java`
  - [ ] Update to filter by specialization, department, faculty
  - [ ] Handle study year filtering

- [ ] `src/main/java/com/dm/service/RoomService.java`
  - [ ] Add filtering by building
  - [ ] Add filtering by computer labs

**New Services to create:**
- [ ] `src/main/java/com/dm/service/FacultyService.java`
  - [ ] CRUD operations for faculties
  - [ ] Get departments by faculty

- [ ] `src/main/java/com/dm/service/DepartmentService.java`
  - [ ] CRUD operations
  - [ ] Teacher lookup by department

- [ ] `src/main/java/com/dm/service/SpecializationService.java`
  - [ ] CRUD operations
  - [ ] Filter by study cycle

### 10. ⏳ Update Mappers (TO DO)
**New MapStruct mappers:**
- [ ] `src/main/java/com/dm/mapper/FacultyMapper.java`
- [ ] `src/main/java/com/dm/mapper/DepartmentMapper.java`
- [ ] `src/main/java/com/dm/mapper/SpecializationMapper.java`

**DTOs to create:**
- [ ] `FacultyDto` (id, code, name, shortName)
- [ ] `DepartmentDto` (id, code, name, facultyId)
- [ ] `SpecializationDto` (id, code, name, departmentId, studyCycle)

**Update existing mappers:**
- [ ] `TeacherMapper` - Handle firstName/lastName split, multi-departments
- [ ] `GroupMapper` - Handle specialization FK instead of string
- [ ] `RoomMapper` - Add building, computers fields
- [ ] `TimeslotMapper` - Use DayOfWeek enum

### 11. ⏳ Update Views & UI Components (TO DO)
**Vaadin components to update:**
- [ ] Teacher registration/profile views
- [ ] Group selection/display
- [ ] Room management views
- [ ] Schedule creation forms
- [ ] Department filtering

### 12. ⏳ Validation & Testing (TO DO)
- [ ] Compile project (mvn clean compile)
- [ ] Run existing unit tests
- [ ] Create new integration tests for new entities
- [ ] Test migration script execution
- [ ] Verify all foreign key relationships
- [ ] Test multi-department teacher scenarios

---

## 🔄 PHASE 3: Integration & Deployment (FUTURE)

### Pre-Deployment Checklist
- [ ] Code review all new/modified files
- [ ] Performance testing (query optimization)
- [ ] Load testing (concurrent schedule creation)
- [ ] Data backup before migration
- [ ] Staging environment validation
- [ ] User acceptance testing (UAT)
- [ ] Documentation updates for end-users

### Deployment Steps
1. [ ] Backup production database
2. [ ] Apply Flyway migrations in order (V1-V8)
3. [ ] Run sanity checks on data integrity
4. [ ] Deploy updated application code
5. [ ] Monitor logs for errors
6. [ ] Verify schedule functionality
7. [ ] User training and rollout

---

## 📊 Current Status Summary

**Completed:**
- ✅ Database schema v2.0 (normalized, 13 tables)
- ✅ 8 Flyway migration scripts (V1-V8)
- ✅ Complete data seeding (faculties, departments, groups, teachers, rooms, timeslots)
- ✅ Architecture documentation (2 comprehensive documents)

**In Progress:**
- 🔄 Entity layer updates (pending)
- 🔄 Domain model alignment (pending)
- 🔄 Repository/Service refactoring (pending)

**Time to Completion (Estimated):**
- Entity updates: 2-3 hours
- Service/Repository: 3-4 hours
- Testing & validation: 2-3 hours
- **Total remaining: 7-10 hours**

---

## 📞 Troubleshooting Guide

### Common Issues & Solutions

#### Migration Fails with "Sequence Not Found"
**Cause:** Sequence creation failed in V1
**Solution:** Check V1 script execution, run `ALTER SEQUENCE ... NOCACHE`

#### Duplicate Key Violations
**Cause:** Running V4-V8 multiple times without cleanup
**Solution:** Execute `DELETE FROM ... CASCADE` for test data, or create new schema

#### Foreign Key Constraint Errors
**Cause:** Inserting data in wrong order (child before parent)
**Solution:** Verify V4 > V5 > V6 > V7 > V8 sequence execution

#### Teacher Department Assignment Fails
**Cause:** Teacher_id or department_id doesn't exist
**Solution:** Check V6 teachers match V4 departments in IDs

#### Room Booking Conflicts
**Cause:** Schedule entries overlapping timeslots/rooms
**Solution:** Implement conflict detection query before INSERT

---

## 📚 Files Modified/Created

### Schema Scripts
- ✅ V1__init_normalized_schema.sql (UPDATED)
- ✅ V2__migrate_legacy_data.sql (unchanged)
- ✅ V3__seed_base_data.sql (DEPRECATED)
- ✅ V4__seed_faculties_departments.sql (NEW)
- ✅ V5__seed_specializations_groups.sql (NEW)
- ✅ V6__seed_teachers.sql (NEW)
- ✅ V7__seed_rooms.sql (NEW)
- ✅ V8__seed_timeslots.sql (NEW)

### Documentation
- ✅ DATABASE_DESIGN.md (NEW)
- ✅ SCHEMA_DIAGRAM.md (NEW)
- ✅ MIGRATION_CHECKLIST.md (THIS FILE)

### Java Code (PENDING)
- ⏳ TeacherProfileEntity.java
- ⏳ GroupEntity.java
- ⏳ FacultyEntity.java (new)
- ⏳ DepartmentEntity.java (new)
- ⏳ SpecializationEntity.java (new)
- ⏳ All corresponding repositories, services, mappers, DTOs

---

**Last Updated:** December 15, 2025  
**Database:** Oracle 11g+  
**Framework:** Spring Boot 3.5.8  
**Migration Tool:** Flyway 9.22.3
