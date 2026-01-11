
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
@Route(value = "courses", layout = MainLayout.class)
@PageTitle("Courses | USV Schedule GA")
public class CourseView extends VerticalLayout {
    Grid<CourseDto> grid = new Grid<>(CourseDto.class);
    CourseForm form;
    CourseService courseService;
    com.dm.service.DepartmentService departmentService;
    com.dm.service.SecretaryService secretaryService;

    public CourseView(CourseService courseService,
            com.dm.service.DepartmentService departmentService,
            com.dm.service.SecretaryService secretaryService) {
        this.courseService = courseService;
        this.departmentService = departmentService;
        this.secretaryService = secretaryService;

        addClassName("course-view");
        setSizeFull();
        configureGrid();
        configureForm();

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        com.dm.view.components.GoogleIcon headerIcon = new com.dm.view.components.GoogleIcon("menu_book");
        headerIcon.getStyle().set("font-size", "32px");
        headerIcon.addClassNames(com.vaadin.flow.theme.lumo.LumoUtility.TextColor.PRIMARY);
        com.vaadin.flow.component.html.H2 pageTitle = new com.vaadin.flow.component.html.H2("Manage Courses");
        pageTitle.addClassNames(com.vaadin.flow.theme.lumo.LumoUtility.Margin.NONE);
        header.add(headerIcon, pageTitle);

        add(header, getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setCourse(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        if (isSecretary()) {
            String email = org.springframework.security.core.context.SecurityContextHolder.getContext()
                    .getAuthentication().getName();
            secretaryService.findByUserEmail(email).ifPresent(profile -> {
                grid.setItems(courseService.findByDepartment(profile.getDepartment().getId()));
            });
        } else {
            grid.setItems(courseService.findAll());
        }
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
        java.util.List<com.dm.dto.DepartmentDto> departmentDtos = departmentService.getAll().stream()
                .map(dept -> new com.dm.dto.DepartmentDto(
                        dept.getId(),
                        dept.getCode(),
                        dept.getName(),
                        dept.getFaculty().getId(),
                        null, null)) // simplified DTO mapping
                .toList();

        form = new CourseForm(departmentDtos);
        form.setWidth("25em");
        form.addSaveListener(this::saveCourse);
        form.addDeleteListener(this::deleteCourse);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveCourse(CourseForm.SaveEvent event) {
        CourseDto courseDto = event.getCourse();
        // If secretary, enforce their department if it's missing (though form should
        // handle it)
        if (isSecretary()) {
            String email = org.springframework.security.core.context.SecurityContextHolder.getContext()
                    .getAuthentication().getName();
            secretaryService.findByUserEmail(email).ifPresent(profile -> {
                // We rely on the form having set the value correctly, or we could force it here
                // But DTO is immutable, so we'd need to reconstruct.
                // Assuming form field matches DTO structure via Binder.
            });
        }

        courseService.save(courseDto);
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
        grid.setColumns("code", "title", "departmentName", "componentType", "credits", "semester", "parity");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editCourse(e.getValue()));
    }

    private void editCourse(CourseDto course) {
        if (course == null) {
            closeEditor();
        } else {
            form.setCourse(course);

            if (isSecretary()) {
                form.department.setReadOnly(true);
                // Ensure the secretary's department is set on the form if it's a new course
                if (course.getId() == null) {
                    String email = org.springframework.security.core.context.SecurityContextHolder.getContext()
                            .getAuthentication().getName();
                    secretaryService.findByUserEmail(email).ifPresent(profile -> {
                        com.dm.data.entity.DepartmentEntity dept = profile.getDepartment();
                        com.dm.dto.DepartmentDto deptDto = new com.dm.dto.DepartmentDto(dept.getId(), dept.getCode(),
                                dept.getName(), dept.getFaculty().getId(), null, null);
                        form.department.setValue(deptDto);
                    });
                }
            } else {
                form.department.setReadOnly(false);
            }

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
        editCourse(new CourseDto(null, null, null, null, 0, 0, null, null, null));
    }

    private boolean isSecretary() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SECRETARY"));
    }
}
