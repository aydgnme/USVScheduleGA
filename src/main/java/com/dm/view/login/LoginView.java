package com.dm.view.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | USV Schedule GA")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Apply a gradient background (can be moved to CSS, but inline for sure-fire
        // applied style test)
        // Better to use a class and handle in styles.css for clean separation.
        // We'll rely on the body background or set a specific class for the view
        // container.
        // But since this is a View component, we can style the host.
        // Let's use a container style.

        // Container Card
        VerticalLayout card = new VerticalLayout();
        card.addClassName("card");
        card.setWidth("400px");
        card.setPadding(true);
        card.setSpacing(true);
        card.setAlignItems(Alignment.CENTER);

        H1 title = new H1("USV Schedule");
        title.getStyle().set("color", "var(--clarity-primary)");
        title.getStyle().set("margin-bottom", "0");

        Paragraph subtitle = new Paragraph("Optimization System");
        subtitle.addClassName("text-secondary");
        subtitle.getStyle().set("margin-top", "0");

        login.setAction("login");
        login.setForgotPasswordButtonVisible(false); // No forgot password for internal app usually

        // Customize the LoginForm
        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Sign In");
        i18nForm.setUsername("Email Address");
        i18nForm.setSubmit("Access Dashboard");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nError = i18n.getErrorMessage();
        i18nError.setTitle("Authentication Failed");
        i18nError.setMessage("Check your email and password.");
        i18n.setErrorMessage(i18nError);

        login.setI18n(i18n);

        card.add(title, subtitle, login);
        add(card);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
