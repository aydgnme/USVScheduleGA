
package com.dm.view.course;

import com.dm.dto.CourseDto;
import com.dm.service.CourseService;
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
@Route(value="courses", layout = MainLayout.class)
@PageTitle("Courses | USV Schedule GA")
public class CourseView extends VerticalLayout {
    Grid<CourseDto> grid = new Grid<>(CourseDto.class);
    CourseForm form;
    CourseService courseService;

    public CourseView(CourseService courseService) {
        this.courseService = courseService;
        addClassName("course-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setCourse(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(courseService.findAll());
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
        form = new CourseForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveCourse);
        form.addDeleteListener(this::deleteCourse);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveCourse(CourseForm.SaveEvent event) {
        // TODO: Convert CourseDto to CourseRequestDto
        // courseService.save(event.getCourse());
        updateList();
        closeEditor();
    }

    private void deleteCourse(CourseForm.DeleteEvent event) {
        if (event.getCourse().getId() != null) {
            courseService.delete(event.getCourse().getId());
        }
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassName("course-grid");
        grid.setSizeFull();
        grid.setColumns("code", "title", "type", "duration", "parity", "teacherName", "groupName");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editCourse(e.getValue()));
    }

    private void editCourse(CourseDto course) {
        if (course == null) {
            closeEditor();
        } else {
            form.setCourse(course);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        Button addCourseButton = new Button("New course");
        addCourseButton.addClickListener(e -> addCourse());

        HorizontalLayout toolbar = new HorizontalLayout(addCourseButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addCourse() {
        grid.asSingleSelect().clear();
        editCourse(new CourseDto(null, null, null, null, 0, 0, null));
    }
}
