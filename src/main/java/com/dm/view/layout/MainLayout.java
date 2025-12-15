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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinConfig;
import com.vaadin.flow.server.VaadinSession;
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
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.MEDIUM);

        Anchor logoutLink = new Anchor("/logout", "Logout");
        logoutLink.getElement().setAttribute("router-ignore", "true");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logoutLink);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo); // Make the logo take up most of the space
        header.setWidth("100%");
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);

    }

    private void createDrawer() {
        VerticalLayout drawer = new VerticalLayout();

        // Role-specific dashboard entry
        if (hasRole("ROLE_ADMIN")) {
            drawer.add(new RouterLink("Dashboard", AdminDashboardView.class));
            drawer.add(new RouterLink("Timeslots", TimeslotView.class));
            drawer.add(new RouterLink("Courses", CourseView.class));
            drawer.add(new RouterLink("Rooms", RoomView.class));
        } else if (hasRole("ROLE_SECRETARY")) {
            drawer.add(new RouterLink("Dashboard", SecretaryDashboardView.class));
            drawer.add(new RouterLink("Timeslots", TimeslotView.class));
            drawer.add(new RouterLink("Courses", CourseView.class));
            drawer.add(new RouterLink("Rooms", RoomView.class));
        } else if (hasRole("ROLE_TEACHER")) {
            drawer.add(new RouterLink("Dashboard", TeacherDashboardView.class));
            drawer.add(new RouterLink("My Courses", com.dm.view.teacher.TeacherCoursesView.class));
            drawer.add(new RouterLink("My Schedule", com.dm.view.teacher.TeacherScheduleView.class));
            drawer.add(new RouterLink("Availability", com.dm.view.teacher.TeacherAvailabilityView.class));
        } else {
            // Fallback for authenticated users without a specific role
            drawer.add(new RouterLink("Dashboard", DashboardView.class));
        }

        addToDrawer(drawer);
    }

    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

}
