package com.dm;

import com.dm.config.DotenvLoader;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@Push
@SpringBootApplication
@PWA(
        name = "USV Schedule GA",
        shortName = "USV GA",
        offlinePath = "offline.html",
        offlineResources = { "images/offline.png" }
)
@Theme("my-theme")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        DotenvLoader.load();
        SpringApplication.run(Application.class, args);
    }
}
