package com.dm.view.admin;

import com.dm.data.entity.DepartmentEntity;
import com.dm.data.entity.SpecializationEntity;
import com.dm.service.DepartmentService;
import com.dm.service.SpecializationService;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/specializations", layout = MainLayout.class)
@PageTitle("Specialization Management | USV Schedule")
@RolesAllowed("ROLE_ADMIN")
public class AdminSpecializationView extends VerticalLayout {

    private final SpecializationService specializationService;
    private final DepartmentService departmentService;

    private Grid<SpecializationEntity> grid;
    private TextField code;
    private TextField name;
    private ComboBox<String> studyCycle;
    private ComboBox<DepartmentEntity> department;

    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private Binder<SpecializationEntity> binder;

    private SpecializationEntity currentSpecialization;

    public AdminSpecializationView(SpecializationService specializationService, DepartmentService departmentService) {
        this.specializationService = specializationService;
        this.departmentService = departmentService;

        addClassName("admin-spec-view");
        setSpacing(false);
        setSizeFull();

        PageHeader header = new PageHeader("Specialization Management");

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

        SearchToolbar toolbar = new SearchToolbar("Search specializations...", this::onSearch);
        Button addButton = new Button("New Specialization", e -> clearForm());
        toolbar.addPrimaryAction(addButton);

        grid = new Grid<>(SpecializationEntity.class, false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();

        grid.addColumn(SpecializationEntity::getCode).setHeader("Code").setSortable(true).setAutoWidth(true);
        grid.addColumn(SpecializationEntity::getName).setHeader("Name").setSortable(true).setFlexGrow(1);
        grid.addColumn(SpecializationEntity::getStudyCycle).setHeader("Cycle").setSortable(true).setAutoWidth(true);
        grid.addColumn(spec -> spec.getDepartment() != null ? spec.getDepartment().getCode() : "-")
                .setHeader("Department").setSortable(true).setAutoWidth(true);

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

        studyCycle = new ComboBox<>("Study Cycle");
        studyCycle.setItems("BACHELOR", "MASTER", "DOCTORATE", "CONVERSION");

        department = new ComboBox<>("Department");
        department.setItemLabelGenerator(DepartmentEntity::getName);
        department.setItems(departmentService.getAll());

        formLayout.add(code, name, studyCycle, department);

        binder = new BeanValidationBinder<>(SpecializationEntity.class);
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
        grid.setItems(specializationService.getAll().stream()
                .filter(s -> matches(s, searchTerm))
                .toList());
    }

    private boolean matches(SpecializationEntity s, String term) {
        if (term == null || term.isEmpty())
            return true;
        String lower = term.toLowerCase();
        return (s.getName() != null && s.getName().toLowerCase().contains(lower)) ||
                (s.getCode() != null && s.getCode().toLowerCase().contains(lower));
    }

    private void refreshGrid() {
        grid.setItems(specializationService.getAll());
    }

    private void populateForm(SpecializationEntity value) {
        this.currentSpecialization = value;
        binder.readBean(value);
        deleteButton.setVisible(true);
    }

    private void clearForm() {
        this.currentSpecialization = null;
        binder.readBean(new SpecializationEntity());
        grid.asSingleSelect().clear();
        deleteButton.setVisible(false);
    }

    private void save() {
        SpecializationEntity entity = new SpecializationEntity();
        if (this.currentSpecialization != null) {
            entity = this.currentSpecialization;
        }

        try {
            if (binder.writeBeanIfValid(entity)) {
                if (entity.getId() == null) {
                    specializationService.create(entity);
                } else {
                    specializationService.update(entity.getId(), entity);
                }
                refreshGrid();
                clearForm();
                Notification.show("Specialization saved successfully")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        } catch (Exception e) {
            Notification.show("Error saving: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void delete() {
        if (currentSpecialization == null)
            return;
        try {
            specializationService.delete(currentSpecialization.getId());
            refreshGrid();
            clearForm();
            Notification.show("Specialization deleted")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Cannot delete: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
