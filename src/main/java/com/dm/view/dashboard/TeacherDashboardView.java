package com.dm.view.dashboard;

import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "teacher", layout = MainLayout.class)
@PageTitle("Dashboard | USV Schedule GA")
@RolesAllowed("TEACHER")
public class TeacherDashboardView extends VerticalLayout {

    public TeacherDashboardView() {
        add(new H1("Teacher Dashboard"));
    }
}
