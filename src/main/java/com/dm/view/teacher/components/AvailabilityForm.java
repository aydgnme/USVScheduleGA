package com.dm.view.teacher.components;

import com.dm.dto.TeacherDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

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
        // Available days field
        availableDaysField = new MultiSelectComboBox<>("Available Days");
        availableDaysField.setItems("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        availableDaysField.setPlaceholder("Select days...");
        availableDaysField.setWidthFull();

        // Preferred time field
        preferredTimeField = new ComboBox<>("Preferred Time");
        preferredTimeField.setItems("Morning", "Afternoon", "Evening", "No preference");
        preferredTimeField.setPlaceholder("Select preferred time...");
        preferredTimeField.setWidthFull();

        // Max hours per week
        maxHoursField = new IntegerField("Max Hours per Week");
        maxHoursField.setValue(40);
        maxHoursField.setMin(1);
        maxHoursField.setMax(60);
        maxHoursField.setWidthFull();

        // Buttons
        saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        cancelButton = new Button("Cancel");

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);

        // Add to form
        add(availableDaysField, preferredTimeField, maxHoursField, buttonLayout);
        setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("500px", 2)
        );
        setColspan(buttonLayout, 2);
    }

    public void setTeacher(TeacherDto teacher) {
        this.currentTeacher = teacher;
        
        // Parse available days (comma-separated string to Set)
        if (teacher.getAvailableDays() != null && !teacher.getAvailableDays().isEmpty()) {
            Set<String> days = new HashSet<>(Arrays.asList(teacher.getAvailableDays().split(",")));
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
            currentTeacher.getName(),
            currentTeacher.getEmail(),
            maxHours,
            currentTeacher.getDepartment(),
            availableDays,
            preferredTime
        );
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}
