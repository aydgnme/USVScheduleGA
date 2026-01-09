package com.dm.view.teacher;

import com.dm.dto.ScheduleEntryDto;
import com.dm.service.ScheduleService;
import com.dm.view.layout.MainLayout;
import com.dm.view.teacher.components.ScheduleGridComponent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * View for displaying the weekly teaching schedule for the logged-in teacher.
 */
@Route(value = "teacher/schedule", layout = MainLayout.class)
@PageTitle("My Schedule | USV Schedule GA")
@RolesAllowed("TEACHER")
public class TeacherScheduleView extends VerticalLayout {

    private final ScheduleService scheduleService;
    private final ScheduleGridComponent scheduleGrid;

    public TeacherScheduleView(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
        this.scheduleGrid = new ScheduleGridComponent();

        addClassName("teacher-schedule-view");
        setSizeFull();
        setPadding(true);

        H1 header = new H1("📅 My Weekly Schedule");
        Paragraph description = new Paragraph(
            "Your complete teaching schedule showing all assigned time slots, courses, and rooms."
        );

        add(header, description, scheduleGrid);
        loadSchedule();
    }

    private void loadSchedule() {
        String teacherEmail = getCurrentUserEmail();
        
        if (teacherEmail == null) {
            Notification.show("Session expired. Please login again.", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        List<ScheduleEntryDto> scheduleItems = scheduleService.getByTeacherEmail(teacherEmail);
        
        if (scheduleItems.isEmpty()) {
            Notification.show("No schedule entries found. Your schedule may not be finalized yet.", 
                    3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        }
        
        scheduleGrid.setScheduleItems(scheduleItems);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
