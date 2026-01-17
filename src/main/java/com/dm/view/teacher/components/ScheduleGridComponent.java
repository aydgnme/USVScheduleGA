package com.dm.view.teacher.components;

import com.dm.dto.ScheduleEntryDto;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.List;

public class ScheduleGridComponent extends VerticalLayout {

    private Grid<ScheduleEntryDto> grid = new Grid<>(ScheduleEntryDto.class);

    public ScheduleGridComponent() {
        setSizeFull();
        configureGrid();
        add(grid);
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn(ScheduleEntryDto::getDayOfWeek).setHeader("Day");
        grid.addColumn(ScheduleEntryDto::getStartTime).setHeader("Start");
        grid.addColumn(ScheduleEntryDto::getEndTime).setHeader("End");
        grid.addColumn(ScheduleEntryDto::getCourseTitle).setHeader("Course");
        grid.addColumn(ScheduleEntryDto::getRoomCode).setHeader("Room");
        grid.addColumn(ScheduleEntryDto::getGroupCode).setHeader("Group");
    }

    public void setScheduleItems(List<ScheduleEntryDto> items) {
        grid.setItems(items);
    }
}
