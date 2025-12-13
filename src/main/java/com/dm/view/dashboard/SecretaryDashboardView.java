package com.dm.view.dashboard;

import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "secretary", layout = MainLayout.class)
@PageTitle("Secretary Dashboard | USV Schedule GA")
@RolesAllowed("SECRETARY")
public class SecretaryDashboardView extends VerticalLayout {

    public SecretaryDashboardView() {
        add(new H1("Secretary Dashboard"));
    }
}
