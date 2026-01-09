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

    public AdminDashboardView() {
        addClassName("admin-dashboard-view");
        setSizeFull();
        setPadding(true);

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        GoogleIcon dashIcon = new GoogleIcon("dashboard");
        dashIcon.getStyle().set("font-size", "32px");
        header.add(dashIcon);

        // Navigation cards
        HorizontalLayout navigationCards = createNavigationCards();

        add(header, navigationCards);
    }

    private HorizontalLayout createNavigationCards() {
        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setSpacing(true);
        cards.setJustifyContentMode(JustifyContentMode.CENTER);

        cards.add(
                createIconNavigationCard("school", "admin/teachers", "Manage Teachers"),
                createIconNavigationCard("meeting_room", "admin/rooms", "Manage Rooms"),
                createIconNavigationCard("groups", "admin/groups", "Manage Groups"));

        // Wrap for responsiveness if needed, but 3 cards usually fit
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
