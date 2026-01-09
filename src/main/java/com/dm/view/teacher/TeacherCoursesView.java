package com.dm.view.teacher;

import com.dm.dto.CourseDto;
import com.dm.service.CourseService;
import com.dm.view.layout.MainLayout;
import com.dm.view.teacher.components.CourseGridComponent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.dm.view.components.GoogleIcon;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * View for displaying courses assigned to the logged-in teacher.
 */
@Route(value = "teacher/courses", layout = MainLayout.class)
@PageTitle("My Courses | USV Schedule GA")
@RolesAllowed("TEACHER")
public class TeacherCoursesView extends VerticalLayout {

    private final CourseService courseService;
    private final CourseGridComponent courseGrid;

    public TeacherCoursesView(CourseService courseService) {
        this.courseService = courseService;
        this.courseGrid = new CourseGridComponent();

        addClassName("teacher-courses-view");
        setSizeFull();
        setPadding(true);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        GoogleIcon headerIcon = new GoogleIcon("library_books");
        headerIcon.addClassNames(LumoUtility.TextColor.PRIMARY);
        headerIcon.getStyle().set("font-size", "2.5rem");

        H1 header = new H1("My Courses");
        header.addClassNames(LumoUtility.Margin.NONE);

        headerLayout.add(headerIcon, header);

        Paragraph description = new Paragraph(
                "Below are the courses you are currently assigned to teach.");

        add(headerLayout, description, courseGrid);
        loadCourses();
    }

    private void loadCourses() {
        String teacherEmail = getCurrentUserEmail();

        if (teacherEmail == null) {
            Notification.show("Session expired. Please login again.", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        // Create filtered list of courses for the teacher
        List<CourseDto> courses = courseService.getCoursesByTeacherEmail(teacherEmail);

        if (courses.isEmpty()) {
            Notification.show("No courses available.", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        }

        courseGrid.setCourses(courses);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
