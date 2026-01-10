package com.dm.view.admin;

import com.dm.dto.TeacherDto;
import com.dm.dto.DepartmentDto;
import com.dm.service.TeacherService;
import com.dm.service.DepartmentService;
import com.dm.view.layout.MainLayout;
import com.dm.view.components.GoogleIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.dm.mapper.DepartmentMapper;
import jakarta.annotation.security.RolesAllowed;
import java.util.stream.Collectors;

@Route(value = "admin/teachers", layout = MainLayout.class)
@PageTitle("Manage Teachers | USV Schedule")
@RolesAllowed("ADMIN")
public class AdminTeachersView extends VerticalLayout {

    private final TeacherService teacherService;
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    private Grid<TeacherDto> grid = new Grid<>(TeacherDto.class);
    private TextField filterText = new TextField();
    private ComboBox<DepartmentDto> departmentFilter = new ComboBox<>("Department");

    public AdminTeachersView(TeacherService teacherService, DepartmentService departmentService,
            DepartmentMapper departmentMapper) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;

        addClassName("admin-teachers-view");
        setSizeFull();
        configureGrid();
        configureFilters();

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        GoogleIcon headerIcon = new GoogleIcon("school");
        headerIcon.getStyle().set("font-size", "32px");
        headerIcon.addClassNames(LumoUtility.TextColor.PRIMARY);
        com.vaadin.flow.component.html.H2 pageTitle = new com.vaadin.flow.component.html.H2("Manage Teachers");
        pageTitle.addClassNames(LumoUtility.Margin.NONE);
        header.add(headerIcon, pageTitle);

        add(header, getToolbar(), getContent());
        updateList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.addClassName("clarity-grid");
        grid.setSizeFull();
        grid.removeAllColumns(); // We'll add them manually for control

        grid.addColumn(TeacherDto::getFirstName).setHeader("First Name").setSortable(true);
        grid.addColumn(TeacherDto::getLastName).setHeader("Last Name").setSortable(true);
        grid.addColumn(TeacherDto::getEmail).setHeader("Email").setSortable(true);

        grid.addColumn(teacher -> {
            if (teacher.getDepartments() == null)
                return "";
            return teacher.getDepartments().stream()
                    .map(DepartmentDto::getCode)
                    .collect(Collectors.joining(", "));
        }).setHeader("Departments");

        grid.addColumn(TeacherDto::getMaxHoursWeekly).setHeader("Max Hours");

        // Action Column (Edit)
        grid.addComponentColumn(teacher -> {
            Button editButton = new Button(new GoogleIcon("edit"));
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> editTeacher(teacher));
            return editButton;
        }).setHeader("Actions");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(event -> editTeacher(event.getValue()));
    }

    private void configureFilters() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        departmentFilter.setItems(departmentService.getAll().stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList()));
        departmentFilter.setItemLabelGenerator(DepartmentDto::getName);
        departmentFilter.addValueChangeListener(e -> updateList());
    }

    private Component getToolbar() {
        filterText.setWidth("300px");
        departmentFilter.setWidth("250px");

        Button addTeacherButton = new Button("Add Teacher");
        addTeacherButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addTeacherButton.addClickListener(click -> addTeacher());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, departmentFilter, addTeacherButton);
        toolbar.addClassName("toolbar");
        toolbar.setAlignItems(Alignment.BASELINE);
        return toolbar;
    }

    private void updateList() {
        // Basic filtering logic (can be pushed to service/repo for efficiency later)
        String keyword = filterText.getValue();
        DepartmentDto dept = departmentFilter.getValue();

        grid.setItems(teacherService.getAll().stream()
                .filter(t -> matchesFilter(t, keyword, dept))
                .collect(Collectors.toList()));
    }

    private boolean matchesFilter(TeacherDto teacher, String keyword, DepartmentDto dept) {
        boolean matchesKeyword = true;
        boolean matchesDept = true;

        if (keyword != null && !keyword.isEmpty()) {
            String lowercaseKeyword = keyword.toLowerCase();
            matchesKeyword = teacher.getFirstName().toLowerCase().contains(lowercaseKeyword) ||
                    teacher.getLastName().toLowerCase().contains(lowercaseKeyword);
        }

        if (dept != null) {
            matchesDept = teacher.getDepartments().stream()
                    .anyMatch(d -> d.getId().equals(dept.getId()));
        }

        return matchesKeyword && matchesDept;
    }

    private void editTeacher(TeacherDto teacher) {
        if (teacher == null)
            return;
        Notification.show("Editing teacher: " + teacher.getLastName());
        // Dialog implementation to follow
    }

    private void addTeacher() {
        grid.asSingleSelect().clear();
        Notification.show("Add teacher clicked");
        // Dialog implementation to follow
    }
}
