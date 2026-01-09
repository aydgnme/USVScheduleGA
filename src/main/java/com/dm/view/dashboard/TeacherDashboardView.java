package com.dm.view.dashboard;

import com.dm.dto.CourseDto;
import com.dm.dto.TeacherDto;
import com.dm.service.CourseService;
import com.dm.service.TeacherService;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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

        H1 header = new H1("👩‍🏫 Teacher Dashboard");
        Paragraph welcome = new Paragraph(
                "Welcome to your teaching dashboard. Quick access to your courses and schedule.");
        welcome.addClassNames(LumoUtility.TextColor.SECONDARY);

        // Navigation cards
        HorizontalLayout navigationCards = createNavigationCards();

        // Course overview section
        H2 coursesHeader = new H2("📚 My Courses");
        courseListLayout.setPadding(false);
        courseListLayout.setSpacing(true);

        add(header, welcome, navigationCards, coursesHeader, courseListLayout);

        loadDashboardData();
    }

    private HorizontalLayout createNavigationCards() {
        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setSpacing(true);

        cards.add(
                createNavigationCard("My Courses", "View all courses you teach",
                        VaadinIcon.BOOK, "teacher/courses"),
                createNavigationCard("My Schedule", "View your weekly schedule",
                        VaadinIcon.CALENDAR, "teacher/schedule"),
                createNavigationCard("Availability", "Set your teaching preferences",
                        VaadinIcon.CLOCK, "teacher/availability"));

        return cards;
    }

    private VerticalLayout createNavigationCard(String title, String description,
            VaadinIcon icon, String route) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Padding.LARGE);
        card.setWidth("33%");
        card.setAlignItems(Alignment.CENTER);

        Icon cardIcon = icon.create();
        cardIcon.setSize("48px");
        cardIcon.addClassNames(LumoUtility.TextColor.PRIMARY);

        H3 cardTitle = new H3(title);
        cardTitle.addClassNames(LumoUtility.Margin.NONE);

        Paragraph cardDescription = new Paragraph(description);
        cardDescription.addClassNames(
                LumoUtility.TextColor.SECONDARY,
                LumoUtility.FontSize.SMALL,
                LumoUtility.TextAlignment.CENTER);

        Span linkText = new Span("Go →");
        linkText.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.TextColor.PRIMARY);

        card.add(cardIcon, cardTitle, cardDescription, linkText);

        // Make the entire card clickable
        card.getStyle().set("cursor", "pointer");
        card.addClickListener(e -> card.getUI().ifPresent(ui -> ui.navigate(route)));

        return card;
    }

    private void loadDashboardData() {
        String teacherEmail = getCurrentUserEmail();

        if (teacherEmail == null) {
            Notification.show("Session expired. Please login again.", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        // Load teacher info
        TeacherDto teacher = teacherService.findByEmail(teacherEmail);
        if (teacher != null) {
            // Could display teacher-specific info here if needed
        }

        // Load courses for the specific teacher
        List<CourseDto> courses = courseService.getCoursesByTeacherEmail(teacherEmail);

        if (courses.isEmpty()) {
            courseListLayout.add(new Paragraph("No courses available."));
        } else {
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

        Icon bookIcon = VaadinIcon.BOOK.create();
        bookIcon.addClassNames(LumoUtility.TextColor.PRIMARY);

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
