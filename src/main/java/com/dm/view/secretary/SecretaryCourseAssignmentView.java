package com.dm.view.secretary;

import com.dm.dto.CourseDto;
import com.dm.dto.CourseOfferingDto;
import com.dm.dto.GroupDto;
import com.dm.dto.TeacherDto;
import com.dm.model.types.WeekParity;
import com.dm.service.CourseOfferingService;
import com.dm.service.CourseService;
import com.dm.service.GroupService;
import com.dm.service.TeacherService;
import com.dm.service.SecretaryService;
import com.dm.view.components.AppCard;
import com.dm.view.components.PageHeader;
import com.dm.view.components.SearchToolbar;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "secretary/assignments", layout = MainLayout.class)
@PageTitle("Course Assignments | USV Schedule")
@RolesAllowed({ "ROLE_SECRETARY", "ROLE_ADMIN" })
public class SecretaryCourseAssignmentView extends VerticalLayout {

    private final CourseOfferingService offeringService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final SecretaryService secretaryService;

    private Long currentDepartmentId;
    private Long currentFacultyId;
    private boolean isSecretary;

    private Grid<CourseOfferingDto> grid;
    private ComboBox<GroupDto> groupFilter;

    // Editor fields
    private ComboBox<CourseDto> course;
    private ComboBox<GroupDto> group;
    private ComboBox<TeacherDto> teacher;
    private IntegerField weeklyHours;
    private ComboBox<WeekParity> parity;

    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private Binder<CourseOfferingDto> binder;

    private CourseOfferingDto currentOffering;

    // Caches for display names
    private Map<Long, String> courseNames;
    private Map<Long, String> groupNames;
    private Map<Long, String> teacherNames;

    public SecretaryCourseAssignmentView(
            CourseOfferingService offeringService,
            CourseService courseService,
            GroupService groupService,
            TeacherService teacherService,
            SecretaryService secretaryService) {
        this.offeringService = offeringService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.secretaryService = secretaryService;

        checkRoleAndSetDepartment();

        addClassName("secretary-assignments-view");
        setSpacing(false);
        setSizeFull();

        loadCaches();

        PageHeader header = new PageHeader("Course Assignments (Teacher allocation)");

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(50);

        createGrid(splitLayout);
        createEditor(splitLayout);

        add(header, splitLayout);

        refreshGrid();
        clearForm();
    }

    private void checkRoleAndSetDepartment() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SECRETARY"))) {
            this.isSecretary = true;
            secretaryService.findByUserEmail(auth.getName()).ifPresent(p -> {
                this.currentDepartmentId = p.getDepartment().getId();
                this.currentFacultyId = p.getDepartment().getFaculty().getId();
            });
        }
    }

    private void loadCaches() {
        courseNames = courseService.findAll().stream()
                .collect(Collectors.toMap(CourseDto::getId, CourseDto::getTitle, (a, b) -> a));

        groupNames = groupService.getAll().stream()
                .collect(Collectors.toMap(GroupDto::getId, GroupDto::getCode, (a, b) -> a));

        teacherNames = teacherService.getAll().stream()
                .collect(Collectors.toMap(TeacherDto::getId, t -> t.getLastName() + " " + t.getFirstName(),
                        (a, b) -> a));
    }

    private void createGrid(SplitLayout splitLayout) {
        AppCard card = new AppCard();
        card.setHeightFull();

        groupFilter = new ComboBox<>("Filter by Group");
        if (currentDepartmentId != null) {
            groupFilter.setItems(groupService.findByDepartmentId(currentDepartmentId));
        } else if (isSecretary) {
            groupFilter.setItems(java.util.Collections.emptyList());
        } else {
            groupFilter.setItems(groupService.getAll());
        }
        groupFilter.setItemLabelGenerator(GroupDto::getCode);
        groupFilter.addValueChangeListener(e -> refreshGrid());
        groupFilter.setClearButtonVisible(true);
        groupFilter.setWidth("200px");

        SearchToolbar toolbar = new SearchToolbar("Search...", term -> {
        }); // Search not impl yet
        toolbar.addFilter(groupFilter);
        Button addButton = new Button("Add Allocation", e -> clearForm());
        toolbar.addPrimaryAction(addButton);

        grid = new Grid<>(CourseOfferingDto.class, false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();

        grid.addColumn(o -> groupNames.getOrDefault(o.getGroupId(), "-")).setHeader("Group").setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(o -> courseNames.getOrDefault(o.getCourseId(), "-")).setHeader("Course").setSortable(true)
                .setFlexGrow(1);
        grid.addColumn(o -> teacherNames.getOrDefault(o.getTeacherId(), "-")).setHeader("Teacher").setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(CourseOfferingDto::getType).setHeader("Type").setAutoWidth(true);
        grid.addColumn(CourseOfferingDto::getWeeklyHours).setHeader("Hrs").setAutoWidth(true);

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                populateForm(e.getValue());
            } else {
                clearForm();
            }
        });

        card.add(toolbar, grid);
        splitLayout.addToPrimary(card);
    }

    private void createEditor(SplitLayout splitLayout) {
        AppCard card = new AppCard();
        card.setHeightFull();

        FormLayout formLayout = new FormLayout();

        group = new ComboBox<>("Group");
        if (currentDepartmentId != null) {
            group.setItems(groupService.findByDepartmentId(currentDepartmentId));
        } else if (isSecretary) {
            group.setItems(java.util.Collections.emptyList());
        } else {
            group.setItems(groupService.getAll());
        }
        group.setItemLabelGenerator(GroupDto::getCode);

        course = new ComboBox<>("Course");
        if (currentFacultyId != null) {
            course.setItems(courseService.findByFacultyId(currentFacultyId));
        } else if (isSecretary) {
            course.setItems(java.util.Collections.emptyList());
        } else {
            course.setItems(courseService.findAll());
        }
        course.setItemLabelGenerator(c -> c.getCode() + " - " + c.getTitle());

        teacher = new ComboBox<>("Teacher");
        if (currentDepartmentId != null) {
            teacher.setItems(teacherService.findByDepartmentId(currentDepartmentId));
        } else if (isSecretary) {
            teacher.setItems(java.util.Collections.emptyList());
        } else {
            teacher.setItems(teacherService.getAll());
        }
        teacher.setItemLabelGenerator(t -> t.getLastName() + " " + t.getFirstName());

        weeklyHours = new IntegerField("Weekly Hours");
        weeklyHours.setMin(1);

        parity = new ComboBox<>("Parity");
        parity.setItems(WeekParity.values());

        ComboBox<com.dm.model.types.ActivityType> type = new ComboBox<>("Type");
        type.setItems(com.dm.model.types.ActivityType.values());

        formLayout.add(group, course, teacher, weeklyHours, parity, type);

        binder = new BeanValidationBinder<>(CourseOfferingDto.class);

        // Manual binding for IDs
        binder.bind(group,
                dto -> {
                    if (dto.getGroupId() == null)
                        return null;
                    return groupService.getAll().stream().filter(g -> g.getId().equals(dto.getGroupId())).findFirst()
                            .orElse(null);
                },
                (dto, g) -> dto.setGroupId(g != null ? g.getId() : null));

        binder.bind(course,
                dto -> {
                    if (dto.getCourseId() == null)
                        return null;
                    return courseService.findAll().stream().filter(c -> c.getId().equals(dto.getCourseId())).findFirst()
                            .orElse(null);
                },
                (dto, c) -> dto.setCourseId(c != null ? c.getId() : null));

        binder.bind(teacher,
                dto -> {
                    if (dto.getTeacherId() == null)
                        return null;
                    return teacherService.getAll().stream().filter(t -> t.getId().equals(dto.getTeacherId()))
                            .findFirst().orElse(null);
                },
                (dto, t) -> dto.setTeacherId(t != null ? t.getId() : null));

        binder.forField(weeklyHours)
                .asRequired("Hours required")
                .bind(CourseOfferingDto::getWeeklyHours, CourseOfferingDto::setWeeklyHours);

        binder.bind(parity, CourseOfferingDto::getParity, CourseOfferingDto::setParity);
        binder.bind(type, CourseOfferingDto::getType, CourseOfferingDto::setType);

        saveButton = new Button("Save Assignment", e -> save());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancelButton = new Button("Cancel", e -> clearForm());

        deleteButton = new Button("Delete", e -> delete());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.setVisible(false);

        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        actions.setSpacing(true);

        card.add(formLayout, actions);
        splitLayout.addToSecondary(card);
    }

    private void refreshGrid() {
        if (groupFilter.getValue() != null) {
            grid.setItems(offeringService.getByGroupId(groupFilter.getValue().getId()));
        } else {
            if (currentDepartmentId != null) {
                grid.setItems(offeringService.getByDepartmentId(currentDepartmentId));
            } else if (isSecretary) {
                grid.setItems(java.util.Collections.emptyList());
            } else {
                grid.setItems(offeringService.getAll());
            }
        }
        // Refresh caches potentially if needed
    }

    private void populateForm(CourseOfferingDto value) {
        this.currentOffering = value;
        binder.readBean(value);
        deleteButton.setVisible(true);
    }

    private void clearForm() {
        this.currentOffering = null;
        CourseOfferingDto dto = new CourseOfferingDto();
        // pre-fill group if filter selected
        if (groupFilter.getValue() != null) {
            dto.setGroupId(groupFilter.getValue().getId());
        }
        dto.setWeeklyHours(2); // Default
        dto.setParity(WeekParity.BOTH);
        binder.readBean(dto);
        grid.asSingleSelect().clear();
        deleteButton.setVisible(false);
    }

    private void save() {
        CourseOfferingDto dto = new CourseOfferingDto();
        if (this.currentOffering != null) {
            dto.setId(this.currentOffering.getId());
        }

        try {
            if (binder.writeBeanIfValid(dto)) {
                offeringService.save(dto);
                refreshGrid();
                clearForm();
                Notification.show("Assignment saved")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void delete() {
        if (currentOffering == null)
            return;
        try {
            offeringService.delete(currentOffering.getId());
            refreshGrid();
            clearForm();
            Notification.show("Assignment deleted")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Cannot delete: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
