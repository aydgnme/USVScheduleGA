package com.dm.view.admin;

import com.dm.dto.RoomDto;
import com.dm.model.types.RoomType;
import com.dm.service.RoomService;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/rooms", layout = MainLayout.class)
@PageTitle("Room Management | USV Schedule")
@RolesAllowed("ROLE_ADMIN")
public class AdminRoomsView extends VerticalLayout {

    private final RoomService roomService;

    private Grid<RoomDto> grid;
    private TextField code;
    private ComboBox<RoomType> roomType;
    private IntegerField capacity;
    private TextField building;

    // Additional fields from DTO if any? RoomDto has id, code, roomType, capacity,
    // building.

    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private Binder<RoomDto> binder;

    private RoomDto currentRoom;

    public AdminRoomsView(RoomService roomService) {
        this.roomService = roomService;

        addClassName("admin-rooms-view");
        setSpacing(false);
        setSizeFull();

        PageHeader header = new PageHeader("Room Management");

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

        SearchToolbar toolbar = new SearchToolbar("Search rooms...", this::onSearch);
        Button addButton = new Button("New Room", e -> clearForm());
        toolbar.addPrimaryAction(addButton);

        grid = new Grid<>(RoomDto.class, false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();

        grid.addColumn(RoomDto::getCode).setHeader("Code").setSortable(true).setAutoWidth(true);
        grid.addColumn(RoomDto::getRoomType).setHeader("Type").setSortable(true).setAutoWidth(true);
        grid.addColumn(RoomDto::getCapacity).setHeader("Cap").setSortable(true).setAutoWidth(true);
        grid.addColumn(RoomDto::getBuilding).setHeader("Building").setSortable(true).setFlexGrow(1);

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
        roomType = new ComboBox<>("Type");
        roomType.setItems(RoomType.values());

        capacity = new IntegerField("Capacity");
        building = new TextField("Building");

        formLayout.add(code, roomType, capacity, building);

        binder = new BeanValidationBinder<>(RoomDto.class);
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
        grid.setItems(roomService.getAll().stream()
                .filter(r -> matches(r, searchTerm))
                .toList());
    }

    private boolean matches(RoomDto r, String term) {
        if (term == null || term.isEmpty())
            return true;
        String lower = term.toLowerCase();
        return (r.getCode() != null && r.getCode().toLowerCase().contains(lower)) ||
                (r.getBuilding() != null && r.getBuilding().toLowerCase().contains(lower));
    }

    private void refreshGrid() {
        grid.setItems(roomService.getAll());
    }

    private void populateForm(RoomDto value) {
        this.currentRoom = value; // DTO usually detached, but we need ID
        binder.readBean(value);
        deleteButton.setVisible(true);
    }

    private void clearForm() {
        this.currentRoom = null;
        binder.readBean(new RoomDto());
        grid.asSingleSelect().clear();
        deleteButton.setVisible(false);
    }

    private void save() {
        RoomDto dto = new RoomDto();
        if (this.currentRoom != null) {
            dto = this.currentRoom; // Preserves ID?
            // Actually, binder.writeBean writes to the passed object.
            // If populateForm passed a DTO from the grid, that DTO has the ID.
            // But binder.readBean makes a copy or reads properties.
            // binder.writeBean(dto) writes fields.
            // If I reuse 'currentRoom', I am modifying the object in the grid if it's the
            // same ref.
            // Actually with DTOs, getting from Service logic might return new instances.
            // Safer to use the ID from currentRoom if present.
        }

        try {
            if (binder.writeBeanIfValid(dto)) {
                roomService.save(dto);
                refreshGrid();
                clearForm();
                Notification.show("Room saved successfully")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        } catch (Exception e) {
            Notification.show("Error saving: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void delete() {
        if (currentRoom == null)
            return;
        try {
            roomService.delete(currentRoom.getId());
            refreshGrid();
            clearForm();
            Notification.show("Room deleted")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Cannot delete: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
