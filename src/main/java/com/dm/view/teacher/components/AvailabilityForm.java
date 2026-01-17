package com.dm.view.teacher.components;

import com.dm.dto.TeacherDto;
import com.dm.view.components.GoogleIcon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Reusable form component for editing teacher availability.
 */
public class AvailabilityForm extends FormLayout {

    private final MultiSelectComboBox<String> availableDaysField;
    private final ComboBox<String> preferredTimeField;
    private final IntegerField maxHoursField;
    private final Button saveButton;
    private final Button cancelButton;

    private TeacherDto currentTeacher;

    public AvailabilityForm() {
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2));

        // Section 1: Schedule Preferences
        H4 scheduleHeader = new H4("Schedule Preferences");
        scheduleHeader.addClassNames(LumoUtility.Margin.Top.MEDIUM, LumoUtility.Margin.Bottom.SMALL);

        HorizontalLayout scheduleTitle = new HorizontalLayout(new GoogleIcon("calendar_month"), scheduleHeader);
        scheduleTitle.setAlignItems(FlexComponent.Alignment.CENTER);
        scheduleTitle.getStyle().set("color", "var(--clarity-primary)");

        availableDaysField = new MultiSelectComboBox<>("Available Days");
        availableDaysField.setItems("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        availableDaysField.setPlaceholder("Select days...");
        availableDaysField.setWidthFull();

        preferredTimeField = new ComboBox<>("Preferred Time");
        preferredTimeField.setItems("Morning", "Afternoon", "Evening", "No preference");
        preferredTimeField.setPlaceholder("Select preferred time...");
        preferredTimeField.setWidthFull();

        // Section 2: Workload Limits
        H4 workloadHeader = new H4("Workload Limits");
        workloadHeader.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.SMALL);

        HorizontalLayout workloadTitle = new HorizontalLayout(new GoogleIcon("timer"), workloadHeader);
        workloadTitle.setAlignItems(FlexComponent.Alignment.CENTER);
        workloadTitle.getStyle().set("color", "var(--clarity-primary)");

        maxHoursField = new IntegerField("Max Hours per Week");
        maxHoursField.setValue(40);
        maxHoursField.setMin(1);
        maxHoursField.setMax(60);
        maxHoursField.setWidthFull();
        maxHoursField.setHelperText("Standard full-time load is 40 hours.");

        // Buttons
        saveButton = new Button("Save Changes");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancelButton = new Button("Revert");

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);
        buttonLayout.addClassName(LumoUtility.Margin.Top.LARGE);

        // Add to form with Colspans for headers
        add(scheduleTitle, availableDaysField, preferredTimeField);
        setColspan(scheduleTitle, 2);

        add(workloadTitle, maxHoursField);
        setColspan(workloadTitle, 2);

        add(buttonLayout);
        setColspan(buttonLayout, 2);
    }

    public void setTeacher(TeacherDto teacher) {
        this.currentTeacher = teacher;

        // Parse available days (comma-separated string to Set)
        if (teacher.getAvailableDaysJson() != null && !teacher.getAvailableDaysJson().isEmpty()) {
            Set<String> days = new HashSet<>(Arrays.asList(teacher.getAvailableDaysJson().split(",")));
            availableDaysField.setValue(days);
        } else {
            availableDaysField.clear();
        }

        // Set preferred time
        if (teacher.getPreferredTime() != null && !teacher.getPreferredTime().isEmpty()) {
            preferredTimeField.setValue(teacher.getPreferredTime());
        } else {
            preferredTimeField.setValue("No preference");
        }

        // Set max hours
        if (teacher.getMaxHoursWeekly() != null) {
            maxHoursField.setValue(teacher.getMaxHoursWeekly());
        } else {
            maxHoursField.setValue(40);
        }
    }

    public TeacherDto getUpdatedTeacher() {
        if (currentTeacher == null) {
            return null;
        }

        // Convert selected days to comma-separated string
        String availableDays = String.join(",", availableDaysField.getValue());
        String preferredTime = preferredTimeField.getValue();
        Integer maxHours = maxHoursField.getValue();

        return new TeacherDto(
                currentTeacher.getId(),
                currentTeacher.getUserId(),
                currentTeacher.getFirstName(),
                currentTeacher.getLastName(),
                currentTeacher.getEmail(),
                maxHours,
                currentTeacher.getDepartments(),
                availableDays,
                preferredTime,
                currentTeacher.getNote());
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}
