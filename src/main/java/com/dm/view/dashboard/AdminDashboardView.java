package com.dm.view.dashboard;

import com.dm.view.layout.MainLayout;
import com.dm.view.components.GoogleIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Admin Dashboard | USV Schedule GA")
@RolesAllowed("ADMIN")
public class AdminDashboardView extends VerticalLayout {

    private final com.dm.service.TeacherService teacherService;
    private final com.dm.service.RoomService roomService;
    private final com.dm.service.GroupService groupService;
    private final com.dm.service.CourseService courseService;
    private final com.dm.service.FacultyService facultyService;
    private final com.dm.service.DepartmentService departmentService;
    private final com.dm.service.SpecializationService specializationService;

    public AdminDashboardView(com.dm.service.TeacherService teacherService,
            com.dm.service.RoomService roomService,
            com.dm.service.GroupService groupService,
            com.dm.service.CourseService courseService,
            com.dm.service.FacultyService facultyService,
            com.dm.service.DepartmentService departmentService,
            com.dm.service.SpecializationService specializationService) {
        this.teacherService = teacherService;
        this.roomService = roomService;
        this.groupService = groupService;
        this.courseService = courseService;
        this.facultyService = facultyService;
        this.departmentService = departmentService;
        this.specializationService = specializationService;

        addClassName("admin-dashboard-view");
        setSizeFull();
        setPadding(true);

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        GoogleIcon dashIcon = new GoogleIcon("dashboard");
        dashIcon.getStyle().set("font-size", "32px");
        header.add(dashIcon);

        // Stats
        HorizontalLayout stats = createStatsRow();

        // Navigation cards
        com.vaadin.flow.component.Component navigationCards = createNavigationCards();

        add(header, stats, navigationCards);
    }

    private com.vaadin.flow.component.Component createNavigationCards() {
        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setSpacing(true);
        cards.setJustifyContentMode(JustifyContentMode.CENTER);

        cards.add(
                createIconNavigationCard("school", "admin/teachers", "Teachers"),
                createIconNavigationCard("meeting_room", "admin/rooms", "Rooms"),
                createIconNavigationCard("groups", "admin/groups", "Groups"),
                createIconNavigationCard("schedule", "admin/timeslots", "Timeslots"));

        // Second Row for Structure
        HorizontalLayout cards2 = new HorizontalLayout();
        cards2.setWidthFull();
        cards2.setSpacing(true);
        cards2.setJustifyContentMode(JustifyContentMode.CENTER);

        cards2.add(
                createIconNavigationCard("domain", "admin/faculties", "Faculties"),
                createIconNavigationCard("account_balance", "admin/departments", "Departments"),
                createIconNavigationCard("class", "admin/specializations", "Specializations"));

        cards.getStyle().set("flex-wrap", "wrap");
        cards2.getStyle().set("flex-wrap", "wrap");

        VerticalLayout wrapper = new VerticalLayout(cards, cards2);
        wrapper.setPadding(false);
        wrapper.setSpacing(true);
        wrapper.setWidthFull();

        return wrapper;
    }

    private HorizontalLayout createStatsRow() {
        HorizontalLayout stats = new HorizontalLayout();
        stats.setWidthFull();
        stats.setJustifyContentMode(JustifyContentMode.CENTER);
        stats.setSpacing(true);
        stats.getStyle().set("flex-wrap", "wrap");

        // Primary Metrics
        stats.add(new com.dm.view.components.StatsCard("Teachers", String.valueOf(teacherService.count()), "school",
                LumoUtility.TextColor.PRIMARY));
        stats.add(new com.dm.view.components.StatsCard("Rooms", String.valueOf(roomService.count()), "meeting_room",
                LumoUtility.TextColor.SUCCESS));
        stats.add(new com.dm.view.components.StatsCard("Groups", String.valueOf(groupService.count()), "groups",
                LumoUtility.TextColor.ERROR));
        stats.add(new com.dm.view.components.StatsCard("Courses", String.valueOf(courseService.count()), "book",
                LumoUtility.TextColor.SECONDARY));

        // Structural Metrics
        // Structural Metrics
        stats.add(new com.dm.view.components.StatsCard("Faculties", String.valueOf(facultyService.count()), "domain",
                LumoUtility.TextColor.BODY));
        stats.add(new com.dm.view.components.StatsCard("Departments", String.valueOf(departmentService.count()),
                "account_balance", LumoUtility.TextColor.BODY));
        stats.add(new com.dm.view.components.StatsCard("Specs", String.valueOf(specializationService.count()), "school",
                LumoUtility.TextColor.BODY));

        return stats;
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
}
