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

@Route(value = "secretary", layout = MainLayout.class)
@PageTitle("Secretary Dashboard | USV Schedule")
@RolesAllowed("SECRETARY")
public class SecretaryDashboardView extends VerticalLayout {

        private final com.dm.service.TimeslotService timeslotService;
        private final com.dm.service.CourseService courseService;
        private final com.dm.service.RoomService roomService;
        private final com.dm.service.CourseOfferingService courseOfferingService;
        private final com.dm.service.TeacherService teacherService;
        private final com.dm.service.GroupService groupService;

        public SecretaryDashboardView(com.dm.service.TimeslotService timeslotService,
                        com.dm.service.CourseService courseService,
                        com.dm.service.RoomService roomService,
                        com.dm.service.CourseOfferingService courseOfferingService,
                        com.dm.service.TeacherService teacherService,
                        com.dm.service.GroupService groupService) {
                this.timeslotService = timeslotService;
                this.courseService = courseService;
                this.roomService = roomService;
                this.courseOfferingService = courseOfferingService;
                this.teacherService = teacherService;
                this.groupService = groupService;

                addClassName("secretary-dashboard-view");
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

        private HorizontalLayout createStatsRow() {
                HorizontalLayout stats = new HorizontalLayout();
                stats.setWidthFull();
                stats.setJustifyContentMode(JustifyContentMode.CENTER);
                stats.setSpacing(true);
                stats.getStyle().set("flex-wrap", "wrap");

                // Assignments Status
                stats.add(new com.dm.view.components.StatsCard("Assignments",
                                String.valueOf(courseOfferingService.count()),
                                "playlist_add_check", LumoUtility.TextColor.PRIMARY));

                // Resources
                stats.add(new com.dm.view.components.StatsCard("Courses", String.valueOf(courseService.count()),
                                "menu_book",
                                LumoUtility.TextColor.SECONDARY));
                stats.add(new com.dm.view.components.StatsCard("Teachers", String.valueOf(teacherService.count()),
                                "school",
                                LumoUtility.TextColor.SECONDARY));
                stats.add(new com.dm.view.components.StatsCard("Groups", String.valueOf(groupService.count()), "groups",
                                LumoUtility.TextColor.SECONDARY));
                stats.add(new com.dm.view.components.StatsCard("Rooms", String.valueOf(roomService.count()),
                                "meeting_room",
                                LumoUtility.TextColor.SUCCESS));

                return stats;
        }

        private com.vaadin.flow.component.Component createNavigationCards() {
                HorizontalLayout cards = new HorizontalLayout();
                cards.setWidthFull();
                cards.setSpacing(true);
                cards.setJustifyContentMode(JustifyContentMode.CENTER);

                cards.add(
                                createIconNavigationCard("playlist_add_check", "secretary/assignments",
                                                "Course Assignments"),
                                createIconNavigationCard("calendar_view_week", "secretary/schedule", "Schedule Editor"),
                                createIconNavigationCard("schedule", "admin/timeslots", "View Timeslots") // Direct
                                                                                                          // access to
                                                                                                          // timeslots
                                                                                                          // is useful
                );

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
}
