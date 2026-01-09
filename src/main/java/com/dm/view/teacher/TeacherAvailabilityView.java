package com.dm.view.teacher;

import com.dm.dto.TeacherDto;
import com.dm.service.TeacherService;
import com.dm.view.layout.MainLayout;
import com.dm.view.teacher.components.AvailabilityForm;
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

/**
 * View for teachers to manage their availability preferences.
 */
@Route(value = "teacher/availability", layout = MainLayout.class)
@PageTitle("My Availability | USV Schedule GA")
@RolesAllowed("TEACHER")
public class TeacherAvailabilityView extends VerticalLayout {

    private final TeacherService teacherService;
    private final AvailabilityForm availabilityForm;
    private TeacherDto currentTeacher;

    public TeacherAvailabilityView(TeacherService teacherService) {
        this.teacherService = teacherService;
        this.availabilityForm = new AvailabilityForm();

        addClassName("teacher-availability-view");
        setSizeFull();
        setPadding(true);

        H1 header = new H1("⏰ My Availability");
        Paragraph description = new Paragraph(
            "Configure your teaching availability preferences. " +
            "This information helps the scheduler optimize course assignments."
        );

        add(header, description, availabilityForm);
        
        // Setup button listeners
        availabilityForm.getSaveButton().addClickListener(e -> saveAvailability());
        availabilityForm.getCancelButton().addClickListener(e -> loadAvailability());

        loadAvailability();
    }

    private void loadAvailability() {
        String teacherEmail = getCurrentUserEmail();
        
        if (teacherEmail == null) {
            Notification.show("Session expired. Please login again.", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        currentTeacher = teacherService.findByEmail(teacherEmail);
        
        if (currentTeacher == null) {
            Notification.show("Teacher profile not found. Please contact administration.", 
                    3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        availabilityForm.setTeacher(currentTeacher);
    }

    private void saveAvailability() {
        try {
            TeacherDto updatedTeacher = availabilityForm.getUpdatedTeacher();
            
            if (updatedTeacher == null) {
                Notification.show("No teacher data to save.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            teacherService.update(updatedTeacher.getId(), updatedTeacher);
            currentTeacher = updatedTeacher;

            Notification notification = Notification.show(
                "✅ Availability preferences saved successfully!", 
                3000, 
                Notification.Position.TOP_CENTER
            );
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        } catch (Exception e) {
            Notification.show("Error saving availability: " + e.getMessage(), 
                    5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
