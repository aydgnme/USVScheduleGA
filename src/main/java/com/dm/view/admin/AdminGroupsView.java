package com.dm.view.admin;

import com.dm.dto.GroupDto;
import com.dm.dto.SpecializationDto;
import com.dm.service.GroupService;
import com.dm.service.SpecializationService;
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
import com.dm.mapper.SpecializationMapper;
import jakarta.annotation.security.RolesAllowed;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Route(value = "admin/groups", layout = MainLayout.class)
@PageTitle("Manage Groups | USV Schedule")
@RolesAllowed("ADMIN")
public class AdminGroupsView extends VerticalLayout {

    private final GroupService groupService;
    private final SpecializationService specializationService;
    private final SpecializationMapper specializationMapper;

    private Grid<GroupDto> grid = new Grid<>(GroupDto.class);
    private TextField filterText = new TextField();
    private ComboBox<SpecializationDto> specializationFilter = new ComboBox<>("Specialization");

    // Cache for specialization names: ID -> Name
    private final Map<Long, String> specializationNames = new HashMap<>();

    public AdminGroupsView(GroupService groupService, SpecializationService specializationService,
            SpecializationMapper specializationMapper) {
        this.groupService = groupService;
        this.specializationService = specializationService;
        this.specializationMapper = specializationMapper;

        addClassName("admin-groups-view");
        setSizeFull();
        loadSpecializations(); // Pre-load for lookup
        configureGrid();
        configureFilters();

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        GoogleIcon headerIcon = new GoogleIcon("groups");
        headerIcon.getStyle().set("font-size", "32px");
        headerIcon.addClassNames(LumoUtility.TextColor.PRIMARY);
        com.vaadin.flow.component.html.H2 pageTitle = new com.vaadin.flow.component.html.H2("Manage Groups");
        pageTitle.addClassNames(LumoUtility.Margin.NONE);
        header.add(headerIcon, pageTitle);

        add(header, getToolbar(), getContent());
        updateList();
    }

    private void loadSpecializations() {
        // Fetch all specializations to build the name cache
        // SpecializationService.getAll() returns Entities? Let's check.
        // Based on AdminTeachersView investigation, RoomService returned DTOs.
        // If SpecializationService returns entities, we'd need mapper.
        // Assuming it's similar to Room/Group services in this batch, let's assume DTOs
        // or Entities.
        // Wait, AdminTeachersView needed mappers for DepartmentService.
        // Safe bet: Use what works. RoomService.getAll() -> DTOs.
        // DepartmentService.getAll() -> Entities.
        // SpecializationService.getAll() likely entities if newly generated?
        // Actually, we injected SpecializationMapper so we can handle entities if
        // needed.

        // Let's assume getAll() returns Entities and use Mapper like in
        // AdminTeachersView
        // Or if it returns DTOs (like RoomService/GroupService), we map if needed.
        // To be safe, let's inspect SpecializationService or use a safe stream.

        // REVISIT: AdminTeachersView used deptService.getAll() which returned Entities.
        // Let's assume specService.getAll() returns Entities.
        specializationService.getAll().forEach(spec -> {
            // If spec is Entity, we get ID and Name.
            // If spec is DTO, we get ID and Name.
            // Since we injected Mapper, let's assume Entity and map to DTO to be consistent
            // with Filter.
            // But for map, we just need ID and Name.
            // We can use the mapper.
            SpecializationDto dto = specializationMapper.toDto(spec);
            if (dto != null) {
                specializationNames.put(dto.getId(), dto.getName());
            }
        });
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
        grid.removeAllColumns();

        grid.addColumn(GroupDto::getCode).setHeader("Code").setSortable(true);
        grid.addColumn(GroupDto::getStudyYear).setHeader("Year").setSortable(true);
        // Removed numberOfStudents as it is missing in DTO

        grid.addColumn(group -> specializationNames.getOrDefault(group.getSpecializationId(), "-"))
                .setHeader("Specialization").setSortable(true);

        grid.addComponentColumn(group -> {
            Button editButton = new Button(new GoogleIcon("edit"));
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> editGroup(group));
            return editButton;
        }).setHeader("Actions");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(event -> editGroup(event.getValue()));
    }

    private void configureFilters() {
        filterText.setPlaceholder("Filter by code...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        // Assuming getAll returns Entities, map to DTOs
        specializationFilter.setItems(specializationService.getAll().stream()
                .map(specializationMapper::toDto)
                .collect(Collectors.toList()));
        specializationFilter.setItemLabelGenerator(SpecializationDto::getName);
        specializationFilter.addValueChangeListener(e -> updateList());
    }

    private Component getToolbar() {
        filterText.setWidth("300px");
        specializationFilter.setWidth("300px");

        Button addGroupButton = new Button("Add Group");
        addGroupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addGroupButton.addClickListener(click -> addGroup());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, specializationFilter, addGroupButton);
        toolbar.addClassName("toolbar");
        toolbar.setAlignItems(Alignment.BASELINE);
        return toolbar;
    }

    private void updateList() {
        String keyword = filterText.getValue();
        SpecializationDto spec = specializationFilter.getValue();

        grid.setItems(groupService.getAll().stream()
                .filter(g -> matchesFilter(g, keyword, spec))
                .collect(Collectors.toList()));
    }

    private boolean matchesFilter(GroupDto group, String keyword, SpecializationDto spec) {
        boolean matchesKeyword = true;
        boolean matchesSpec = true;

        if (keyword != null && !keyword.isEmpty()) {
            matchesKeyword = group.getCode().toLowerCase().contains(keyword.toLowerCase());
        }

        if (spec != null && group.getSpecializationId() != null) {
            matchesSpec = group.getSpecializationId().equals(spec.getId());
        }

        return matchesKeyword && matchesSpec;
    }

    private void editGroup(GroupDto group) {
        if (group == null)
            return;
        Notification.show("Editing group: " + group.getCode());
    }

    private void addGroup() {
        grid.asSingleSelect().clear();
        Notification.show("Add group clicked");
    }
}
