package com.dm.view.dashboard;

import com.dm.dto.CourseDto;
import com.dm.dto.TeacherDto;
// Removed unused imports
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

    private final com.dm.service.CourseService courseService;
    private final com.dm.service.TeacherService teacherService;
    private final com.dm.service.CourseOfferingService courseOfferingService;
    private final VerticalLayout courseListLayout;

    public TeacherDashboardView(com.dm.service.CourseService courseService,
            com.dm.service.TeacherService teacherService,
            com.dm.service.CourseOfferingService courseOfferingService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.courseOfferingService = courseOfferingService;
        this.courseListLayout = new VerticalLayout();

        addClassName("teacher-dashboard-view");
        setSizeFull();
        setPadding(true);

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        GoogleIcon dashIcon = new GoogleIcon("dashboard");
        dashIcon.getStyle().set("font-size", "32px");
        header.add(dashIcon);

        // Stats Placeholder (populated later)
        HorizontalLayout statsContainer = new HorizontalLayout();
        statsContainer.setWidthFull();
        statsContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        statsContainer.setId("stats-container");

        // Navigation cards
        com.vaadin.flow.component.Component navigationCards = createNavigationCards();

        // Course overview section
        HorizontalLayout coursesHeader = new HorizontalLayout();
        coursesHeader.setAlignItems(Alignment.CENTER);
        GoogleIcon coursesIcon = new GoogleIcon("library_books");
        coursesIcon.getStyle().set("font-size", "24px");
        coursesHeader.add(coursesIcon);

        courseListLayout.setPadding(false);
        courseListLayout.setSpacing(true);

        add(header, statsContainer, navigationCards, coursesHeader, courseListLayout);

        loadDashboardData(statsContainer);
    }

    private com.vaadin.flow.component.Component createNavigationCards() {
        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setSpacing(true);
        cards.setJustifyContentMode(JustifyContentMode.CENTER);

        cards.add(
                createIconNavigationCard("class", "teacher/courses", "My Courses"),
                createIconNavigationCard("calendar_month", "teacher/schedule", "My Schedule"),
                createIconNavigationCard("event_available", "teacher/availability", "Availability"));

        cards.getStyle().set("flex-wrap", "wrap");

        return cards;
    }

    private VerticalLayout createIconNavigationCard(String iconName, String route, String tooltip) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.MEDIUM,
                "card-hover");
        card.setWidth("150px");
        card.setHeight("150px");
        card.setAlignItems(Alignment.CENTER);
        card.setJustifyContentMode(JustifyContentMode.CENTER);

        GoogleIcon cardIcon = new GoogleIcon(iconName);
        cardIcon.getStyle().set("font-size", "64px");
        cardIcon.addClassNames(LumoUtility.TextColor.PRIMARY);

        card.getElement().setAttribute("title", tooltip);
        card.add(cardIcon);

        card.getStyle().set("cursor", "pointer");
        card.addClickListener(e -> card.getUI().ifPresent(ui -> ui.navigate(route)));

        return card;
    }

    private void loadDashboardData(HorizontalLayout statsContainer) {
        String teacherEmail = getCurrentUserEmail();

        if (teacherEmail == null) {
            Notification.show("Session expired.", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        TeacherDto teacher = teacherService.findByUserEmail(teacherEmail);
        if (teacher == null) {
            courseListLayout.add(new Paragraph("Teacher profile not found."));
            return;
        }

        // Get Offerings (Actual workload)
        List<com.dm.dto.CourseOfferingDto> offerings = courseOfferingService.getByTeacherId(teacher.getId());

        int totalHours = offerings.stream().mapToInt(com.dm.dto.CourseOfferingDto::getWeeklyHours).sum();
        int courseCount = offerings.size();

        // Calculate credits by fetching Course details (could be optimized)
        int totalCredits = 0;

        if (offerings.isEmpty()) {
            courseListLayout.add(new Paragraph("No active course assignments."));
        } else {
            for (com.dm.dto.CourseOfferingDto offering : offerings) {
                // Fetch full course details for credits, etc.
                java.util.Optional<com.dm.dto.CourseDto> courseOpt = courseService.findById(offering.getCourseId());
                if (courseOpt.isPresent()) {
                    com.dm.dto.CourseDto course = courseOpt.get();
                    totalCredits += course.getCredits();
                    courseListLayout.add(createCourseCard(offering, course));
                } else {
                    // Fallback if course not found (rare)
                    courseListLayout.add(new Paragraph("Course data missing for ID: " + offering.getCourseId()));
                }
            }
        }

        // Populate Stats
        statsContainer.setSpacing(true);
        statsContainer.getStyle().set("flex-wrap", "wrap");

        statsContainer.add(
                new com.dm.view.components.StatsCard("Assignments", String.valueOf(courseCount), "class",
                        LumoUtility.TextColor.PRIMARY),
                new com.dm.view.components.StatsCard("Weekly Hours", String.valueOf(totalHours), "schedule",
                        LumoUtility.TextColor.WARNING),
                new com.dm.view.components.StatsCard("Total Credits", String.valueOf(totalCredits), "school",
                        LumoUtility.TextColor.SUCCESS));
    }

    private HorizontalLayout createCourseCard(com.dm.dto.CourseOfferingDto offering, com.dm.dto.CourseDto course) {
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

        // Title: Code - Title (Group)
        String titleText = course.getCode() + " - " + course.getTitle();
        if (offering.getGroupCode() != null) {
            titleText += " (" + offering.getGroupCode() + ")";
        }

        Span courseTitle = new Span(titleText);
        courseTitle.addClassNames(LumoUtility.FontWeight.SEMIBOLD);

        Span courseDetails = new Span(
                course.getCredits() + " cr • " +
                        offering.getWeeklyHours() + " hrs/week • " +
                        (offering.getParity() != null ? offering.getParity() : "All Weeks"));
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
