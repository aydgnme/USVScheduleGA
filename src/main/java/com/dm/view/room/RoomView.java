
package com.dm.view.room;

import com.dm.dto.RoomDto;
import com.dm.service.RoomService;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value="rooms", layout = MainLayout.class)
@PageTitle("Rooms | USV Schedule GA")
public class RoomView extends VerticalLayout {
    Grid<RoomDto> grid = new Grid<>(RoomDto.class);
    RoomForm form;
    RoomService roomService;

    public RoomView(RoomService roomService) {
        this.roomService = roomService;
        addClassName("room-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setRoom(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(roomService.findAll());
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new RoomForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveRoom);
        form.addDeleteListener(this::deleteRoom);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveRoom(RoomForm.SaveEvent event) {
        roomService.save(event.getRoom());
        updateList();
        closeEditor();
    }

    private void deleteRoom(RoomForm.DeleteEvent event) {
        roomService.delete(event.getRoom());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassName("room-grid");
        grid.setSizeFull();
        grid.setColumns("code", "type", "capacity");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editRoom(e.getValue()));
    }

    private void editRoom(RoomDto room) {
        if (room == null) {
            closeEditor();
        } else {
            form.setRoom(room);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        Button addRoomButton = new Button("New room");
        addRoomButton.addClickListener(e -> addRoom());

        HorizontalLayout toolbar = new HorizontalLayout(addRoomButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addRoom() {
        grid.asSingleSelect().clear();
        editRoom(new RoomDto(null, null, 0, null));
    }
}
