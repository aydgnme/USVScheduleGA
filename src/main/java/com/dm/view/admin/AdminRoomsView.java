package com.dm.view.admin;

import com.dm.dto.RoomDto;
import com.dm.model.types.RoomType;
import com.dm.service.RoomService;
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
import jakarta.annotation.security.RolesAllowed;
import java.util.stream.Collectors;

@Route(value = "admin/rooms", layout = MainLayout.class)
@PageTitle("Manage Rooms | USV Schedule")
@RolesAllowed("ADMIN")
public class AdminRoomsView extends VerticalLayout {

    private final RoomService roomService;

    private Grid<RoomDto> grid = new Grid<>(RoomDto.class);
    private TextField filterText = new TextField();
    private ComboBox<RoomType> typeFilter = new ComboBox<>("Type");

    public AdminRoomsView(RoomService roomService) {
        this.roomService = roomService;

        addClassName("admin-rooms-view");
        setSizeFull();
        configureGrid();
        configureFilters();

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        GoogleIcon headerIcon = new GoogleIcon("meeting_room");
        headerIcon.getStyle().set("font-size", "32px");
        headerIcon.addClassNames(LumoUtility.TextColor.PRIMARY);
        com.vaadin.flow.component.html.H2 pageTitle = new com.vaadin.flow.component.html.H2("Manage Rooms");
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
        grid.removeAllColumns();

        grid.addColumn(RoomDto::getCode).setHeader("Code").setSortable(true);
        grid.addColumn(RoomDto::getRoomType).setHeader("Type").setSortable(true);
        grid.addColumn(RoomDto::getCapacity).setHeader("Capacity").setSortable(true);
        grid.addColumn(RoomDto::getBuilding).setHeader("Building").setSortable(true);

        grid.addComponentColumn(room -> {
            Button editButton = new Button(new GoogleIcon("edit"));
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> editRoom(room));
            return editButton;
        }).setHeader("Actions");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.asSingleSelect().addValueChangeListener(event -> editRoom(event.getValue()));
    }

    private void configureFilters() {
        filterText.setPlaceholder("Filter by code/building...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        typeFilter.setItems(RoomType.values());
        typeFilter.addValueChangeListener(e -> updateList());
    }

    private Component getToolbar() {
        filterText.setWidth("300px");
        typeFilter.setWidth("200px");

        Button addRoomButton = new Button("Add Room");
        addRoomButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addRoomButton.addClickListener(click -> addRoom());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, typeFilter, addRoomButton);
        toolbar.addClassName("toolbar");
        toolbar.setAlignItems(Alignment.BASELINE);
        return toolbar;
    }

    private void updateList() {
        String keyword = filterText.getValue();
        RoomType type = typeFilter.getValue();

        grid.setItems(roomService.getAll().stream()
                .filter(r -> matchesFilter(r, keyword, type))
                .collect(Collectors.toList()));
    }

    private boolean matchesFilter(RoomDto room, String keyword, RoomType type) {
        boolean matchesKeyword = true;
        boolean matchesType = true;

        if (keyword != null && !keyword.isEmpty()) {
            String lowercaseKeyword = keyword.toLowerCase();
            matchesKeyword = room.getCode().toLowerCase().contains(lowercaseKeyword) ||
                    room.getBuilding().toLowerCase().contains(lowercaseKeyword);
        }

        if (type != null) {
            matchesType = room.getRoomType() == type;
        }

        return matchesKeyword && matchesType;
    }

    private void editRoom(RoomDto room) {
        if (room == null)
            return;
        Notification.show("Editing room: " + room.getCode());
    }

    private void addRoom() {
        grid.asSingleSelect().clear();
        Notification.show("Add room clicked");
    }
}
