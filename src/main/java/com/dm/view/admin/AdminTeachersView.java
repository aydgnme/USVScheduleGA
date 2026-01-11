package com.dm.view.admin;

import com.dm.dto.DepartmentDto;
import com.dm.dto.TeacherDto;
import com.dm.mapper.DepartmentMapper;
import com.dm.service.DepartmentService;
import com.dm.service.TeacherService;
import com.dm.view.components.AppCard;
import com.dm.view.components.PageHeader;
import com.dm.view.components.SearchToolbar;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "admin/teachers", layout = MainLayout.class)
@PageTitle("Teacher Management | USV Schedule")
@RolesAllowed("ROLE_ADMIN")
public class AdminTeachersView extends VerticalLayout {

    private final TeacherService teacherService;
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    private Grid<TeacherDto> grid;
    private TextField firstName;
    private TextField lastName;
    private EmailField email;
    private IntegerField maxHoursWeekly;
    private MultiSelectComboBox<DepartmentDto> departments;

    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private Binder<TeacherDto> binder;

    private TeacherDto currentTeacher;

    public AdminTeachersView(TeacherService teacherService, DepartmentService departmentService,
            DepartmentMapper departmentMapper) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;

        addClassName("admin-teachers-view");
        setSpacing(false);
        setSizeFull();

        PageHeader header = new PageHeader("Teacher Management");

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(40);

        createGrid(splitLayout);
        createEditor(splitLayout);

        add(header, splitLayout);

        refreshGrid();
        clearForm();
    }

    private void createGrid(SplitLayout splitLayout) {
        AppCard card = new AppCard();
        card.setHeightFull();

        SearchToolbar toolbar = new SearchToolbar("Search teachers...", this::onSearch);
        Button addButton = new Button("New Teacher", e -> clearForm());
        toolbar.addPrimaryAction(addButton);

        grid = new Grid<>(TeacherDto.class, false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();

        grid.addColumn(TeacherDto::getFirstName).setHeader("First Name").setSortable(true).setAutoWidth(true);
        grid.addColumn(TeacherDto::getLastName).setHeader("Last Name").setSortable(true).setAutoWidth(true);
        grid.addColumn(TeacherDto::getEmail).setHeader("Email").setSortable(true).setFlexGrow(1);
        grid.addColumn(t -> t.getDepartments().stream()
                .map(DepartmentDto::getCode)
                .collect(Collectors.joining(", ")))
                .setHeader("Depts").setAutoWidth(true);

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

        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new EmailField("Email");
        maxHoursWeekly = new IntegerField("Max Hours/Week");

        departments = new MultiSelectComboBox<>("Departments");
        departments.setItems(departmentService.getAll().stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList()));
        departments.setItemLabelGenerator(DepartmentDto::getName);

        formLayout.add(firstName, lastName, email, maxHoursWeekly, departments);

        binder = new BeanValidationBinder<>(TeacherDto.class);
        binder.bindInstanceFields(this);

        saveButton = new Button("Save", e -> save());
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

    private void onSearch(String searchTerm) {
        grid.setItems(teacherService.getAll().stream()
                .filter(t -> matches(t, searchTerm))
                .toList());
    }

    private boolean matches(TeacherDto t, String term) {
        if (term == null || term.isEmpty())
            return true;
        String lower = term.toLowerCase();
        return (t.getFirstName() != null && t.getFirstName().toLowerCase().contains(lower)) ||
                (t.getLastName() != null && t.getLastName().toLowerCase().contains(lower)) ||
                (t.getEmail() != null && t.getEmail().toLowerCase().contains(lower));
    }

    private void refreshGrid() {
        grid.setItems(teacherService.getAll());
    }

    private void populateForm(TeacherDto value) {
        this.currentTeacher = value;
        binder.readBean(value);
        deleteButton.setVisible(true);
    }

    private void clearForm() {
        this.currentTeacher = null;
        TeacherDto newDto = new TeacherDto();
        newDto.setDepartments(new HashSet<>()); // Initialize collection
        binder.readBean(newDto);
        grid.asSingleSelect().clear();
        deleteButton.setVisible(false);
    }

    private void save() {
        TeacherDto dto = new TeacherDto();
        if (this.currentTeacher != null) {
            dto = this.currentTeacher;
        } else {
            dto.setDepartments(new HashSet<>());
        }

        try {
            if (binder.writeBeanIfValid(dto)) {
                // Ensure departments are not null
                if (dto.getDepartments() == null) {
                    dto.setDepartments(new HashSet<>());
                }

                teacherService.save(dto);
                refreshGrid();
                clearForm();
                Notification.show("Teacher saved successfully")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        } catch (Exception e) {
            Notification.show("Error saving: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void delete() {
        if (currentTeacher == null)
            return;
        try {
            teacherService.delete(currentTeacher.getId());
            refreshGrid();
            clearForm();
            Notification.show("Teacher deleted")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Cannot delete: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
