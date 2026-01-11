package com.dm.view.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * AppCard
 * 
 * A standard card container for the application.
 * Corresponds to the "Clarity-style" visual language:
 * - White background (base)
 * - Subtle border (contrast-10pct)
 * - Rounded corners (border-radius-m)
 * - Padding (padding-l)
 */
public class AppCard extends Div {

    public AppCard() {
        addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST_10,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Padding.LARGE,
                LumoUtility.BoxShadow.XSMALL,
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Gap.MEDIUM);
        // Ensure it takes available width but respects layout constraints
        setWidthFull();
    }

    /**
     * Adds components to the card.
     * implicitly acts as a vertical stack due to FlexDirection.COLUMN
     */
    public void add(com.vaadin.flow.component.Component... components) {
        super.add(components);
    }
}
