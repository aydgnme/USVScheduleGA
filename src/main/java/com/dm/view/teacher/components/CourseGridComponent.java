package com.dm.view.teacher.components;

import com.dm.dto.CourseDto;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

/**
 * Reusable component for displaying courses in a grid.
 */
public class CourseGridComponent extends VerticalLayout {

    private final Grid<CourseDto> grid;

    public CourseGridComponent() {
        this.grid = new Grid<>(CourseDto.class, false);
        configureGrid();
        add(grid);
        setSizeFull();
        setPadding(false);
    }

    private void configureGrid() {
        grid.addColumn(CourseDto::getCode).setHeader("Code").setSortable(true).setAutoWidth(true);
        grid.addColumn(CourseDto::getTitle).setHeader("Title").setSortable(true).setAutoWidth(true);
        grid.addColumn(CourseDto::getCredits).setHeader("Credits").setSortable(true).setAutoWidth(true);
        grid.addColumn(CourseDto::getSemester).setHeader("Semester").setSortable(true).setAutoWidth(true);
        grid.addColumn(CourseDto::getParity).setHeader("Week Parity").setSortable(true).setAutoWidth(true);
        
        grid.setSizeFull();
        grid.setAllRowsVisible(false);
        grid.setPageSize(20);
    }

    public void setCourses(List<CourseDto> courses) {
        grid.setItems(courses);
    }

    public Grid<CourseDto> getGrid() {
        return grid;
    }
}
