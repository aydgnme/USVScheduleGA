package com.dm.view.teacher;

import com.dm.data.entity.TeacherPreferenceEntity;
import com.dm.dto.TeacherDto;
import com.dm.service.TeacherPreferenceService;
import com.dm.service.TeacherService;
import com.dm.view.layout.MainLayout;
import com.dm.view.teacher.components.AvailabilityForm;
import com.dm.view.teacher.components.TeacherPreferenceForm;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import com.dm.view.components.GoogleIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.TextStyle;
import java.util.Locale;

/**
 * View for teachers to manage their availability preferences.
 */
@Route(value = "teacher/availability", layout = MainLayout.class)
@PageTitle("My Availability | USV Schedule GA")
@RolesAllowed("TEACHER")
public class TeacherAvailabilityView extends VerticalLayout {

    private final TeacherService teacherService;
    private final TeacherPreferenceService preferenceService;
    private final AvailabilityForm availabilityForm;
    private final TeacherPreferenceForm preferenceForm;
    private final Grid<TeacherPreferenceEntity> preferenceGrid;
    private TeacherDto currentTeacher;

    public TeacherAvailabilityView(TeacherService teacherService,
            TeacherPreferenceService preferenceService,
            com.dm.service.CourseService courseService,
            com.dm.service.GroupService groupService) {
        this.teacherService = teacherService;
        this.preferenceService = preferenceService;
        this.availabilityForm = new AvailabilityForm();
        this.preferenceForm = new TeacherPreferenceForm(courseService, groupService);
        this.preferenceGrid = new Grid<>(TeacherPreferenceEntity.class);

        addClassName("teacher-availability-view");
        setSizeFull();
        setPadding(true);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        GoogleIcon headerIcon = new GoogleIcon("event_available");
        headerIcon.addClassNames(LumoUtility.TextColor.PRIMARY);
        headerIcon.getStyle().set("font-size", "2.5rem"); // Match H1 size roughly

        H1 header = new H1("My Availability");
        header.addClassNames(LumoUtility.Margin.NONE);

        headerLayout.add(headerIcon, header);

        Paragraph description = new Paragraph(
                "Configure your teaching availability preferences. " +
                        "This information helps the scheduler optimize course assignments.");
        description.addClassName(LumoUtility.TextColor.SECONDARY);

        // Card Wrapper
        VerticalLayout card = new VerticalLayout();
        card.addClassNames("card"); // Uses the .card style from CSS
        card.setPadding(true);
        card.setSpacing(true);
        card.setMaxWidth("800px"); // Limit width for better readability on large screens

        card.add(description, availabilityForm);

        // --- NEW PREFERENCE SECTION ---
        H2 prefHeader = new H2("Specific Schedule Preferences");
        prefHeader.addClassNames(LumoUtility.Margin.Top.LARGE);

        Paragraph prefDescription = new Paragraph(
                "Add specific constraints (e.g., 'I want to teach Course X on Tuesday').");
        prefDescription.addClassName(LumoUtility.TextColor.SECONDARY);

        configurePreferenceGrid();

        preferenceForm.addSaveListener(this::savePreference);
        preferenceForm.addCloseListener(e -> preferenceForm.setVisible(true)); // Just clear/reset

        card.add(prefHeader, prefDescription, preferenceForm, preferenceGrid);

        add(headerLayout, card);

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
        updatePreferenceList();
    }

    private void configurePreferenceGrid() {
        preferenceGrid.addClassName("preference-grid");
        preferenceGrid.setHeight("300px");
        preferenceGrid.removeAllColumns();

        preferenceGrid.addColumn(p -> p.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .setHeader("Day");

        preferenceGrid.addColumn(
                p -> p.getCourse() != null ? p.getCourse().getCode() + " - " + p.getCourse().getTitle() : "All Courses")
                .setHeader("Course");

        preferenceGrid.addColumn(p -> p.getGroup() != null ? p.getGroup().getCode() : "All Groups")
                .setHeader("Group");

        preferenceGrid.addColumn(p -> p.getStudyYear() != null ? "Year " + p.getStudyYear() : "All Years")
                .setHeader("Study Year");

        preferenceGrid.addComponentColumn(p -> {
            com.vaadin.flow.component.button.Button deleteBtn = new com.vaadin.flow.component.button.Button(
                    new GoogleIcon("delete"));
            deleteBtn.addThemeVariants(com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR,
                    com.vaadin.flow.component.button.ButtonVariant.LUMO_ICON,
                    com.vaadin.flow.component.button.ButtonVariant.LUMO_SMALL);
            deleteBtn.addClickListener(e -> {
                preferenceService.delete(p.getId());
                updatePreferenceList();
            });
            return deleteBtn;
        });
    }

    private void updatePreferenceList() {
        if (currentTeacher != null) {
            preferenceGrid.setItems(preferenceService.getPreferencesByTeacherEmail(currentTeacher.getEmail()));
        }
    }

    private void savePreference(TeacherPreferenceForm.SaveEvent event) {
        TeacherPreferenceEntity p = new TeacherPreferenceEntity();
        p.setDayOfWeek(event.getDay());

        // Map DTOs to partial entities just for ID reference or null
        // Services will handle full hydration if needed, but for simple ID refs:
        if (event.getCourse() != null) {
            com.dm.data.entity.CourseEntity c = new com.dm.data.entity.CourseEntity();
            c.setId(event.getCourse().getId());
            p.setCourse(c);
        }
        if (event.getGroup() != null) {
            com.dm.data.entity.GroupEntity g = new com.dm.data.entity.GroupEntity();
            g.setId(event.getGroup().getId());
            p.setGroup(g);
        }
        p.setStudyYear(event.getStudyYear());
        p.setPriority(1); // Default priority for now

        try {
            preferenceService.save(p, getCurrentUserEmail());
            updatePreferenceList();
            Notification.show("Preference added", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Error adding preference: " + e.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
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
                    Notification.Position.TOP_CENTER);
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
