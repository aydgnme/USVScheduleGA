package com.dm.view.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * StatusBadge
 * 
 * A semantic badge for status indication.
 * Uses Lumo "badge" theme variants.
 */
public class StatusBadge extends Span {

    public enum Type {
        SUCCESS("badge success"),
        ERROR("badge error"),
        WARNING("badge contrast"), // 'contrast' is often used for warning/neutral in Lumo if warning undefined,
                                   // but 'badge warning' exists in some presets. Let's stick to standard Lumo.
        NEUTRAL("badge"),
        CONTRAST("badge contrast");

        private final String theme;

        Type(String theme) {
            this.theme = theme;
        }

        public String getTheme() {
            return theme;
        }
    }

    public StatusBadge(String text, Type type) {
        setText(text);
        getElement().setAttribute("theme", type.getTheme());
        addClassNames(LumoUtility.FontSize.XSMALL, LumoUtility.FontWeight.BOLD);
    }

    // Helper constructor for boolean states
    public StatusBadge(boolean isActive, String trueText, String falseText) {
        this(isActive ? trueText : falseText, isActive ? Type.SUCCESS : Type.CONTRAST);
    }
}
