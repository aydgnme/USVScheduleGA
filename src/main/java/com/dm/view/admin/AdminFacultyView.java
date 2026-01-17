package com.dm.view.admin;

import com.dm.data.entity.FacultyEntity;
import com.dm.service.FacultyService;
import com.dm.view.components.AppCard;
import com.dm.view.components.GoogleIcon;
import com.dm.view.components.PageHeader;
import com.dm.view.components.SearchToolbar;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Optional;

@Route(value = "admin/faculties", layout = MainLayout.class)
@PageTitle("Faculty Management | USV Schedule")
@RolesAllowed("ROLE_ADMIN")
public class AdminFacultyView extends VerticalLayout {

    private final FacultyService facultyService;

    private Grid<FacultyEntity> grid;
    private TextField code;
    private TextField name;
    private TextField shortName;
    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private Binder<FacultyEntity> binder;

    private FacultyEntity currentFaculty;

    public AdminFacultyView(FacultyService facultyService) {
        this.facultyService = facultyService;

        addClassName("admin-view");
        setSpacing(false);
        setSizeFull();

        // Header
        PageHeader header = new PageHeader("Faculty Management");

        // Split Layout
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(40); // 40% for List, 60% for Detail

        createGrid(splitLayout);
        createEditor(splitLayout);

        add(header, splitLayout);

        // Initial Load
        refreshGrid();
        clearForm();
    }

    private void createGrid(SplitLayout splitLayout) {
        AppCard card = new AppCard();
        card.setHeightFull();

        // Toolbar
        SearchToolbar toolbar = new SearchToolbar("Search faculties...", this::onSearch);
        Button addButton = new Button("New Faculty", e -> clearForm());
        toolbar.addPrimaryAction(addButton);

        // Grid
        grid = new Grid<>(FacultyEntity.class, false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();

        grid.addColumn(FacultyEntity::getCode).setHeader("Code").setSortable(true).setAutoWidth(true);
        grid.addColumn(FacultyEntity::getName).setHeader("Name").setSortable(true).setFlexGrow(2);
        grid.addColumn(FacultyEntity::getShortName).setHeader("Short Name").setSortable(true).setAutoWidth(true);

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
        card.setHeightFull(); // Should fill vertical height

        FormLayout formLayout = new FormLayout();

        code = new TextField("Code");
        name = new TextField("Name");
        shortName = new TextField("Short Name");

        formLayout.add(code, name, shortName);

        binder = new BeanValidationBinder<>(FacultyEntity.class);
        binder.bindInstanceFields(this);

        // Actions
        saveButton = new Button("Save", e -> save());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancelButton = new Button("Cancel", e -> clearForm());

        deleteButton = new Button("Delete", e -> delete());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.setVisible(false);

        // Footer
        com.vaadin.flow.component.orderedlayout.HorizontalLayout actions = new com.vaadin.flow.component.orderedlayout.HorizontalLayout(
                saveButton, cancelButton, deleteButton);
        actions.setSpacing(true);

        card.add(formLayout, actions);
        splitLayout.addToSecondary(card);
    }

    private void onSearch(String searchTerm) {
        // Simple search (for Phase 1 MVP, we might fetch all and filter in memory if
        // small list or impl search in service)
        // Since Service is limited:
        // We will just fetch all and filter stream for now.
        grid.setItems(facultyService.getAll().stream()
                .filter(f -> matches(f, searchTerm))
                .toList());
    }

    private boolean matches(FacultyEntity f, String term) {
        if (term == null || term.isEmpty())
            return true;
        String lower = term.toLowerCase();
        return (f.getName() != null && f.getName().toLowerCase().contains(lower)) ||
                (f.getCode() != null && f.getCode().toLowerCase().contains(lower));
    }

    private void refreshGrid() {
        grid.setItems(facultyService.getAll());
    }

    private void populateForm(FacultyEntity value) {
        this.currentFaculty = value;
        binder.readBean(value);
        deleteButton.setVisible(true);
    }

    private void clearForm() {
        this.currentFaculty = null;
        binder.readBean(new FacultyEntity());
        grid.asSingleSelect().clear();
        deleteButton.setVisible(false);
    }

    private void save() {
        FacultyEntity entity = new FacultyEntity();
        if (this.currentFaculty != null) {
            entity = this.currentFaculty; // Update existing instance or used ID
        }

        try {
            if (binder.writeBeanIfValid(entity)) {
                if (entity.getId() == null) {
                    facultyService.create(entity);
                    UI.getCurrent().getPage().open("admin/faculties", "_self"); // Hard refresh to ensure ID sync or
                                                                                // just refresh grid
                    // Actually simpler:
                    // refreshGrid();
                    // clearForm();
                    // Notification.show("Faculty created");
                } else {
                    facultyService.update(entity.getId(), entity);
                }
                refreshGrid();
                clearForm();
                Notification.show("Faculty saved successfully")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        } catch (Exception e) {
            Notification.show("Error saving: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void delete() {
        if (currentFaculty == null)
            return;
        try {
            facultyService.delete(currentFaculty.getId());
            refreshGrid();
            clearForm();
            Notification.show("Faculty deleted")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Cannot delete: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
