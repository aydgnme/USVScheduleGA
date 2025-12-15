package com.dm.view.teacher.components;

import com.dm.dto.ScheduleItemDto;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

/**
 * Reusable component for displaying schedule items in a grid.
 */
public class ScheduleGridComponent extends VerticalLayout {

    private final Grid<ScheduleItemDto> grid;

    public ScheduleGridComponent() {
        this.grid = new Grid<>(ScheduleItemDto.class, false);
        configureGrid();
        add(grid);
        setSizeFull();
        setPadding(false);
    }

    private void configureGrid() {
        grid.addColumn(ScheduleItemDto::getDay).setHeader("Day").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleItemDto::getStartTime).setHeader("Start Time").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleItemDto::getEndTime).setHeader("End Time").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleItemDto::getCourseCode).setHeader("Course Code").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleItemDto::getCourseTitle).setHeader("Course Title").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleItemDto::getRoomName).setHeader("Room").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleItemDto::getGroupName).setHeader("Group").setSortable(true).setAutoWidth(true);
        
        grid.setSizeFull();
        grid.setAllRowsVisible(false);
        grid.setPageSize(20);
    }

    public void setScheduleItems(List<ScheduleItemDto> items) {
        grid.setItems(items);
    }

    public Grid<ScheduleItemDto> getGrid() {
        return grid;
    }
}
