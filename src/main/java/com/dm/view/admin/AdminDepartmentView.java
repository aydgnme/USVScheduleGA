package com.dm.view.admin;

import com.dm.data.entity.DepartmentEntity;
import com.dm.data.entity.FacultyEntity;
import com.dm.service.DepartmentService;
import com.dm.service.FacultyService;
import com.dm.view.components.AppCard;
import com.dm.view.components.PageHeader;
import com.dm.view.components.SearchToolbar;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/departments", layout = MainLayout.class)
@PageTitle("Department Management | USV Schedule")
@RolesAllowed("ROLE_ADMIN")
public class AdminDepartmentView extends VerticalLayout {

    private final DepartmentService departmentService;
    private final FacultyService facultyService;

    private Grid<DepartmentEntity> grid;
    private TextField code;
    private TextField name;
    private ComboBox<FacultyEntity> faculty;

    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private Binder<DepartmentEntity> binder;

    private DepartmentEntity currentDepartment;

    public AdminDepartmentView(DepartmentService departmentService, FacultyService facultyService) {
        this.departmentService = departmentService;
        this.facultyService = facultyService;

        addClassName("admin-dept-view");
        setSpacing(false);
        setSizeFull();

        PageHeader header = new PageHeader("Department Management");

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

        SearchToolbar toolbar = new SearchToolbar("Search departments...", this::onSearch);
        Button addButton = new Button("New Department", e -> clearForm());
        toolbar.addPrimaryAction(addButton);

        grid = new Grid<>(DepartmentEntity.class, false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();

        grid.addColumn(DepartmentEntity::getCode).setHeader("Code").setSortable(true).setAutoWidth(true);
        grid.addColumn(DepartmentEntity::getName).setHeader("Name").setSortable(true).setFlexGrow(1);
        grid.addColumn(dept -> dept.getFaculty() != null ? dept.getFaculty().getCode() : "-")
                .setHeader("Faculty").setSortable(true).setAutoWidth(true);

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

        code = new TextField("Code");
        name = new TextField("Name");
        faculty = new ComboBox<>("Faculty");
        faculty.setItemLabelGenerator(FacultyEntity::getName);
        faculty.setItems(facultyService.getAll());

        formLayout.add(code, name, faculty);

        binder = new BeanValidationBinder<>(DepartmentEntity.class);
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
        grid.setItems(departmentService.getAll().stream()
                .filter(d -> matches(d, searchTerm))
                .toList());
    }

    private boolean matches(DepartmentEntity d, String term) {
        if (term == null || term.isEmpty())
            return true;
        String lower = term.toLowerCase();
        return (d.getName() != null && d.getName().toLowerCase().contains(lower)) ||
                (d.getCode() != null && d.getCode().toLowerCase().contains(lower));
    }

    private void refreshGrid() {
        grid.setItems(departmentService.getAll());
    }

    private void populateForm(DepartmentEntity value) {
        this.currentDepartment = value;
        binder.readBean(value);
        deleteButton.setVisible(true);
    }

    private void clearForm() {
        this.currentDepartment = null;
        binder.readBean(new DepartmentEntity());
        grid.asSingleSelect().clear();
        deleteButton.setVisible(false);
    }

    private void save() {
        DepartmentEntity entity = new DepartmentEntity();
        if (this.currentDepartment != null) {
            entity = this.currentDepartment;
        }

        try {
            if (binder.writeBeanIfValid(entity)) {
                if (entity.getId() == null) {
                    departmentService.create(entity);
                } else {
                    departmentService.update(entity.getId(), entity);
                }
                refreshGrid();
                clearForm();
                Notification.show("Department saved successfully")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        } catch (Exception e) {
            Notification.show("Error saving: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void delete() {
        if (currentDepartment == null)
            return;
        try {
            departmentService.delete(currentDepartment.getId());
            refreshGrid();
            clearForm();
            Notification.show("Department deleted")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Cannot delete (possibly linked): " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
