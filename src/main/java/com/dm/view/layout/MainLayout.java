package com.dm.view.layout;

import com.dm.view.dashboard.AdminDashboardView;
import com.dm.view.dashboard.DashboardView;
import com.dm.view.dashboard.SecretaryDashboardView;
import com.dm.view.dashboard.TeacherDashboardView;
import com.dm.view.timeslot.TimeslotView;
import com.dm.view.course.CourseView;
import com.dm.view.room.RoomView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.dm.view.components.GoogleIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MainLayout extends AppLayout {

        public MainLayout() {
                createHeader();
                createDrawer();
        }

        private void createHeader() {
                H1 logo = new H1("USV Schedule GA");
                logo.addClassNames("app-header-title");

                Anchor logoutLink = new Anchor("/logout", "Logout");
                logoutLink.addClassName("text-secondary");
                logoutLink.getElement().setAttribute("router-ignore", "true");

                HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logoutLink);

                header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
                header.expand(logo);
                header.setWidth("100%");
                header.addClassName("app-header");

                addToNavbar(header);
        }

        private void createDrawer() {
                SideNav nav = new SideNav();

                // Role-specific dashboard entry
                if (hasRole("ROLE_ADMIN")) {
                        nav.addItem(new SideNavItem("Dashboard", AdminDashboardView.class,
                                        new GoogleIcon("dashboard")));

                        SideNavItem managementGroup = new SideNavItem("Management");
                        managementGroup.setPrefixComponent(new GoogleIcon("admin_panel_settings"));
                        managementGroup.addItem(
                                        new SideNavItem("Faculties", com.dm.view.admin.AdminFacultyView.class,
                                                        new GoogleIcon("domain")));
                        managementGroup.addItem(
                                        new SideNavItem("Departments", com.dm.view.admin.AdminDepartmentView.class,
                                                        new GoogleIcon("business")));
                        managementGroup.addItem(
                                        new SideNavItem("Specializations",
                                                        com.dm.view.admin.AdminSpecializationView.class,
                                                        new GoogleIcon("school")));
                        managementGroup.addItem(
                                        new SideNavItem("Teachers", com.dm.view.admin.AdminTeachersView.class,
                                                        new GoogleIcon("school")));
                        managementGroup.addItem(
                                        new SideNavItem("Rooms", com.dm.view.admin.AdminRoomsView.class,
                                                        new GoogleIcon("meeting_room")));
                        managementGroup.addItem(
                                        new SideNavItem("Groups", com.dm.view.admin.AdminGroupsView.class,
                                                        new GoogleIcon("groups")));
                        managementGroup.addItem(
                                        new SideNavItem("Timeslots", com.dm.view.admin.AdminTimeslotView.class,
                                                        new GoogleIcon("schedule")));
                        nav.addItem(managementGroup);

                        SideNavItem academicGroup = new SideNavItem("Academic");
                        academicGroup.setPrefixComponent(new GoogleIcon("school"));
                        academicGroup.addItem(
                                        new SideNavItem("Courses", CourseView.class, new GoogleIcon("menu_book")));
                        academicGroup.addItem(
                                        new SideNavItem("Timeslots", TimeslotView.class, new GoogleIcon("schedule")));
                        nav.addItem(academicGroup);

                        SideNavItem algoGroup = new SideNavItem("Algorithm");
                        algoGroup.setPrefixComponent(new GoogleIcon("psychology"));
                        algoGroup.addItem(new SideNavItem("Generator", com.dm.view.admin.AdminGeneratorView.class,
                                        new GoogleIcon("autorenew")));
                        nav.addItem(algoGroup);

                } else if (hasRole("ROLE_SECRETARY")) {
                        nav.addItem(new SideNavItem("Dashboard", SecretaryDashboardView.class,
                                        new GoogleIcon("dashboard")));
                        nav.addItem(new SideNavItem("Timeslots", TimeslotView.class, new GoogleIcon("schedule")));
                        nav.addItem(new SideNavItem("Course Assignments",
                                        com.dm.view.secretary.SecretaryCourseAssignmentView.class,
                                        new GoogleIcon("assignment_ind")));
                        nav.addItem(new SideNavItem("Schedule Editor",
                                        com.dm.view.secretary.SecretaryScheduleView.class,
                                        new GoogleIcon("edit_calendar")));
                        nav.addItem(new SideNavItem("Courses", CourseView.class, new GoogleIcon("menu_book")));
                        nav.addItem(new SideNavItem("Rooms", RoomView.class, new GoogleIcon("meeting_room")));

                } else if (hasRole("ROLE_TEACHER")) {
                        nav.addItem(new SideNavItem("Dashboard", TeacherDashboardView.class,
                                        new GoogleIcon("dashboard")));
                        nav.addItem(new SideNavItem("My Courses", com.dm.view.teacher.TeacherCoursesView.class,
                                        new GoogleIcon("library_books")));
                        nav.addItem(new SideNavItem("My Schedule", com.dm.view.teacher.TeacherScheduleView.class,
                                        new GoogleIcon("calendar_month")));
                        nav.addItem(new SideNavItem("Availability", com.dm.view.teacher.TeacherAvailabilityView.class,
                                        new GoogleIcon("event_available")));

                } else {
                        // Fallback for authenticated users without a specific role
                        nav.addItem(new SideNavItem("Dashboard", DashboardView.class, new GoogleIcon("dashboard")));
                }

                Scroller scroller = new Scroller(nav);
                scroller.setClassName(LumoUtility.Padding.SMALL);

                addToDrawer(scroller);
        }

        private boolean hasRole(String role) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) {
                        return false;
                }

                // Debug logging
                // System.out.println("User: " + authentication.getName());
                // authentication.getAuthorities().forEach(a -> System.out.println("Auth: " +
                // a.getAuthority()));

                return authentication.getAuthorities().stream()
                                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
        }
}
