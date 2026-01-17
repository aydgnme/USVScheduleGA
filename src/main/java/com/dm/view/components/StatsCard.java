package com.dm.view.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class StatsCard extends VerticalLayout {

    public StatsCard(String title, String count, String iconName, String colorClass) {
        addClassName(LumoUtility.Background.CONTRAST_5);
        addClassName(LumoUtility.BorderRadius.SMALL);
        addClassName(LumoUtility.Padding.MEDIUM);
        setWidth("200px");
        setAlignItems(FlexComponent.Alignment.START);

        GoogleIcon icon = new GoogleIcon(iconName);
        icon.addClassName(colorClass);
        icon.getStyle().set("font-size", "24px");

        Span countSpan = new Span(count);
        countSpan.addClassName(LumoUtility.FontSize.XXLARGE);
        countSpan.addClassName(LumoUtility.FontWeight.BOLD);

        Span titleSpan = new Span(title);
        titleSpan.addClassName(LumoUtility.TextColor.SECONDARY);

        add(icon, countSpan, titleSpan);
    }
}
