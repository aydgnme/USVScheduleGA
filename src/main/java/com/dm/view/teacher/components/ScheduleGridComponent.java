package com.dm.view.teacher.components;

import com.dm.dto.ScheduleEntryDto;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

/**
 * Reusable component for displaying schedule items in a grid.
 */
public class ScheduleGridComponent extends VerticalLayout {

    private final Grid<ScheduleEntryDto> grid;

    public ScheduleGridComponent() {
        this.grid = new Grid<>(ScheduleEntryDto.class, false);
        configureGrid();
        add(grid);
        setSizeFull();
        setPadding(false);
    }

    private void configureGrid() {
        grid.addColumn(ScheduleEntryDto::getDayOfWeek).setHeader("Day").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleEntryDto::getStartTime).setHeader("Start Time").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleEntryDto::getEndTime).setHeader("End Time").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleEntryDto::getCourseCode).setHeader("Course Code").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleEntryDto::getCourseTitle).setHeader("Course Title").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleEntryDto::getRoomCode).setHeader("Room").setSortable(true).setAutoWidth(true);
        grid.addColumn(ScheduleEntryDto::getGroupCode).setHeader("Group").setSortable(true).setAutoWidth(true);
        
        grid.setSizeFull();
        grid.setAllRowsVisible(false);
        grid.setPageSize(20);
    }

    public void setScheduleItems(List<ScheduleEntryDto> items) {
        grid.setItems(items);
    }

    public Grid<ScheduleEntryDto> getGrid() {
        return grid;
    }
}
