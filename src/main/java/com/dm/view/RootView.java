package com.dm.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("")
@AnonymousAllowed
public class RootView extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            event.forwardTo("login");
        } else {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            if ("ROLE_SECRETARY".equals(role)) {
                event.forwardTo("secretary");
            } else if ("ROLE_TEACHER".equals(role)) {
                event.forwardTo("teacher");
            } else if ("ROLE_ADMIN".equals(role)) {
                event.forwardTo("admin");
            } else {
                event.forwardTo("dashboard");
            }
        }
    }
}
