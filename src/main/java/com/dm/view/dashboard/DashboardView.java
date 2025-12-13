package com.dm.view.dashboard;

import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard")
@RolesAllowed({"TEACHER", "SECRETARY", "ADMIN"})
public class DashboardView extends H2 {
    public DashboardView() {
        super("Welcome to USV Schedule GA!");
    }
}
