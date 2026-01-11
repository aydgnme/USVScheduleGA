package com.dm.view.dashboard;

import com.dm.dto.CourseDto;
import com.dm.dto.TeacherDto;
import com.dm.service.CourseService;
import com.dm.service.TeacherService;
import com.dm.view.layout.MainLayout;
import com.dm.view.components.GoogleIcon;
import com.vaadin.flow.component.html.*;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Route(value = "teacher", layout = MainLayout.class)
@PageTitle("Dashboard | USV Schedule GA")
@RolesAllowed("TEACHER")
public class TeacherDashboardView extends VerticalLayout {

    private final CourseService courseService;
    private final TeacherService teacherService;
    private final VerticalLayout courseListLayout;

    public TeacherDashboardView(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.courseListLayout = new VerticalLayout();

        addClassName("teacher-dashboard-view");
        setSizeFull();
        setPadding(true);

        // Minimal Header with Icon
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        GoogleIcon dashIcon = new GoogleIcon("dashboard");
        dashIcon.getStyle().set("font-size", "32px");
        header.add(dashIcon);
        // Note: Removing text "Teacher Dashboard" and "Welcome" as per "yazilari yok
        // et"

        // Navigation cards
        HorizontalLayout navigationCards = createNavigationCards();

        // Course overview section
        HorizontalLayout coursesHeader = new HorizontalLayout();
        coursesHeader.setAlignItems(Alignment.CENTER);
        GoogleIcon coursesIcon = new GoogleIcon("library_books");
        coursesIcon.getStyle().set("font-size", "24px");
        coursesHeader.add(coursesIcon); // Icon only header

        courseListLayout.setPadding(false);
        courseListLayout.setSpacing(true);

        add(header, navigationCards, coursesHeader, courseListLayout);

        loadDashboardData();
    }

    private HorizontalLayout createStatsRow(int courseCount, int totalCredits) {
        HorizontalLayout stats = new HorizontalLayout();
        stats.setWidthFull();
        stats.setJustifyContentMode(JustifyContentMode.CENTER);
        stats.setSpacing(true);
        stats.getStyle().set("flex-wrap", "wrap");

        stats.add(
                new com.dm.view.components.StatsCard("Courses Taught", String.valueOf(courseCount), "class",
                        LumoUtility.TextColor.PRIMARY),
                new com.dm.view.components.StatsCard("Total Credits", String.valueOf(totalCredits), "school",
                        LumoUtility.TextColor.SUCCESS));
        return stats;
    }

    private HorizontalLayout createNavigationCards() {
        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setSpacing(true);
        cards.setJustifyContentMode(JustifyContentMode.CENTER); // Center them

        cards.add(
                createIconNavigationCard("class", "teacher/courses", "My Courses"),
                createIconNavigationCard("calendar_month", "teacher/schedule", "My Schedule"),
                createIconNavigationCard("event_available", "teacher/availability", "Availability"));

        return cards;
    }

    private VerticalLayout createIconNavigationCard(String iconName, String route, String tooltip) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.MEDIUM,
                "card-hover"); // Custom class for hover effect
        card.setWidth("150px"); // Fixed square-ish size
        card.setHeight("150px");
        card.setAlignItems(Alignment.CENTER);
        card.setJustifyContentMode(JustifyContentMode.CENTER);

        GoogleIcon cardIcon = new GoogleIcon(iconName);
        cardIcon.getStyle().set("font-size", "64px"); // Large Icon
        cardIcon.addClassNames(LumoUtility.TextColor.PRIMARY);

        // Tooltip for accessibility/usability since text is gone
        card.getElement().setAttribute("title", tooltip);

        card.add(cardIcon);

        // Make the entire card clickable
        card.getStyle().set("cursor", "pointer");
        card.addClickListener(e -> card.getUI().ifPresent(ui -> ui.navigate(route)));

        return card;
    }

    private void loadDashboardData() {
        String teacherEmail = getCurrentUserEmail();

        if (teacherEmail == null) {
            Notification.show("Session expired.", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        // Load courses for the specific teacher
        List<CourseDto> courses = courseService.getCoursesByTeacherEmail(teacherEmail);

        if (courses.isEmpty()) {
            courseListLayout.add(new Paragraph("No courses available."));
            addComponentAtIndex(1, createStatsRow(0, 0)); // Add stats even if empty
        } else {
            int totalCredits = courses.stream().mapToInt(CourseDto::getCredits).sum();
            addComponentAtIndex(1, createStatsRow(courses.size(), totalCredits)); // Add stats row below header

            for (CourseDto course : courses) {
                courseListLayout.add(createCourseCard(course));
            }
        }
    }

    private HorizontalLayout createCourseCard(CourseDto course) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.SMALL,
                LumoUtility.Padding.MEDIUM);
        card.setWidthFull();
        card.setAlignItems(Alignment.CENTER);

        GoogleIcon bookIcon = new GoogleIcon("menu_book");
        bookIcon.addClassNames(LumoUtility.TextColor.PRIMARY);
        bookIcon.getStyle().set("font-size", "24px");

        VerticalLayout details = new VerticalLayout();
        details.setPadding(false);
        details.setSpacing(false);

        Span courseTitle = new Span(course.getCode() + " - " + course.getTitle());
        courseTitle.addClassNames(LumoUtility.FontWeight.SEMIBOLD);

        Span courseDetails = new Span(
                course.getCredits() + " cr • " +
                        course.getSemester() + " sem • " +
                        (course.getParity() != null ? course.getParity() : "N/A"));
        courseDetails.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        details.add(courseTitle, courseDetails);
        card.add(bookIcon, details);
        card.expand(details);

        return card;
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
