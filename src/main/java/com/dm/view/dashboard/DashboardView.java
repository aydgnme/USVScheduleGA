package com.dm.view.dashboard;

import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | USV Schedule GA")
@RolesAllowed({ "TEACHER", "SECRETARY", "ADMIN", "USER" })
public class DashboardView extends VerticalLayout implements BeforeEnterObserver {

    public DashboardView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Fallback content if redirection doesn't happen (e.g. unknown role)
        add(new H2("Welcome to USV Schedule GA"));
        add(new Span("Select a module from the menu to begin."));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (hasRole("ROLE_ADMIN")) {
            event.forwardTo(AdminDashboardView.class);
        } else if (hasRole("ROLE_SECRETARY")) {
            event.forwardTo(SecretaryDashboardView.class);
        } else if (hasRole("ROLE_TEACHER")) {
            event.forwardTo(TeacherDashboardView.class);
        }
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
