
package com.dm.view.course;

import com.dm.dto.CourseDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class CourseForm extends FormLayout {
    Binder<CourseDto> binder = new BeanValidationBinder<>(CourseDto.class);

    TextField code = new TextField("Code");
    TextField title = new TextField("Title");
    ComboBox<com.dm.model.types.CourseComponentType> componentType = new ComboBox<>("Type");
    public ComboBox<com.dm.dto.DepartmentDto> department = new ComboBox<>("Department");
    IntegerField credits = new IntegerField("Credits");
    IntegerField semester = new IntegerField("Semester");
    ComboBox<com.dm.model.types.WeekParity> parity = new ComboBox<>("Parity");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public CourseForm(java.util.List<com.dm.dto.DepartmentDto> departments) {
        addClassName("course-form");

        componentType.setItems(com.dm.model.types.CourseComponentType.values());
        parity.setItems(com.dm.model.types.WeekParity.values());
        department.setItems(departments);
        department.setItemLabelGenerator(com.dm.dto.DepartmentDto::getName);

        binder.bindInstanceFields(this);

        add(code,
                title,
                department,
                componentType,
                credits,
                semester,
                parity,
                createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setCourse(CourseDto course) {
        binder.setBean(course);
    }

    // Events
    public static abstract class CourseFormEvent extends ComponentEvent<CourseForm> {
        private final CourseDto course;

        protected CourseFormEvent(CourseForm source, CourseDto course) {
            super(source, false);
            this.course = course;
        }

        public CourseDto getCourse() {
            return course;
        }
    }

    public static class SaveEvent extends CourseFormEvent {
        SaveEvent(CourseForm source, CourseDto course) {
            super(source, course);
        }
    }

    public static class DeleteEvent extends CourseFormEvent {
        DeleteEvent(CourseForm source, CourseDto course) {
            super(source, course);
        }

    }

    public static class CloseEvent extends CourseFormEvent {
        CloseEvent(CourseForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
