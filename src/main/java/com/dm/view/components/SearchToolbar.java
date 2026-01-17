package com.dm.view.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.function.Consumer;

/**
 * SearchToolbar
 * 
 * A standard toolbar for DataGrids.
 * Contains:
 * - Search Field (Left)
 * - Filters (Middle - optional)
 * - Primary Action Button (Right - optional)
 */
public class SearchToolbar extends HorizontalLayout {

    private final TextField searchField;
    private final HorizontalLayout actionArea;

    public SearchToolbar(String searchPlaceholder, Consumer<String> onSearch) {
        setWidthFull();
        addClassNames(LumoUtility.Padding.Bottom.SMALL);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        searchField = new TextField();
        searchField.setPlaceholder(searchPlaceholder);
        searchField.setPrefixComponent(new GoogleIcon("search"));
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> onSearch.accept(e.getValue()));
        searchField.setWidth("300px");

        actionArea = new HorizontalLayout();
        actionArea.setSpacing(true);

        add(searchField, actionArea);
    }

    public void addFilter(Component filter) {
        // Add filters after search field, before action area
        // For simplicity in this v1, additional filters could be added to a middle area
        // But typically we want them next to search.
        addComponentAtIndex(1, filter);
    }

    public void addPrimaryAction(Button button) {
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setIcon(new GoogleIcon("add"));
        actionArea.add(button);
    }

    public void addAction(Component component) {
        actionArea.add(component);
    }
}
