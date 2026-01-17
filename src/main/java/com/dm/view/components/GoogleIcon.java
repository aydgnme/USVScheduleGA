package com.dm.view.components;

import com.vaadin.flow.component.html.Span;

/**
 * Helper component to render Google Material Symbols.
 * Usage: new GoogleIcon("home");
 */
public class GoogleIcon extends Span {

    public GoogleIcon(String iconName) {
        addClassName("material-symbols-outlined");
        setText(iconName);
    }

    public static GoogleIcon create(String iconName) {
        return new GoogleIcon(iconName);
    }
}
