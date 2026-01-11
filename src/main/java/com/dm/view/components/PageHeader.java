package com.dm.view.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * PageHeader
 * 
 * Standard header for every main view.
 * Layout:
 * [ Title (H2) | Actions (Right aligned) ]
 * [ Breadcrumbs (Optional) ]
 */
public class PageHeader extends VerticalLayout {

    private final H2 titleComponent;
    private final HorizontalLayout topRow;
    private final HorizontalLayout actions;

    public PageHeader(String titleText, Component... actionComponents) {
        addClassName(LumoUtility.Padding.Bottom.MEDIUM);
        setSpacing(false);
        setPadding(false);

        titleComponent = new H2(titleText);
        titleComponent.addClassNames(LumoUtility.FontSize.XXLARGE, LumoUtility.Margin.NONE);

        actions = new HorizontalLayout();
        actions.setSpacing(true);
        if (actionComponents != null) {
            actions.add(actionComponents);
        }

        topRow = new HorizontalLayout(titleComponent, actions);
        topRow.setWidthFull();
        topRow.setAlignItems(FlexComponent.Alignment.CENTER);
        topRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        add(topRow);
    }

    public void setTitle(String title) {
        titleComponent.setText(title);
    }

    public void addAction(Component action) {
        actions.add(action);
    }
}
