package com.dm.view.timeslot;

import com.dm.dto.TimeslotDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class TimeslotForm extends FormLayout {
    Binder<TimeslotDto> binder = new BeanValidationBinder<>(TimeslotDto.class);

    TextField day = new TextField("Day");
    TextField startTime = new TextField("Start time");
    TextField endTime = new TextField("End time");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public TimeslotForm() {
        addClassName("timeslot-form");
        binder.bindInstanceFields(this);

        add(day,
                startTime,
                endTime,
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
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }


    public void setTimeslot(TimeslotDto timeslot) {
        binder.setBean(timeslot);
    }

    // Events
    public static abstract class TimeslotFormEvent extends ComponentEvent<TimeslotForm> {
        private final TimeslotDto timeslot;

        protected TimeslotFormEvent(TimeslotForm source, TimeslotDto timeslot) {
            super(source, false);
            this.timeslot = timeslot;
        }

        public TimeslotDto getTimeslot() {
            return timeslot;
        }
    }

    public static class SaveEvent extends TimeslotFormEvent {
        SaveEvent(TimeslotForm source, TimeslotDto timeslot) {
            super(source, timeslot);
        }
    }

    public static class DeleteEvent extends TimeslotFormEvent {
        DeleteEvent(TimeslotForm source, TimeslotDto timeslot) {
            super(source, timeslot);
        }

    }

    public static class CloseEvent extends TimeslotFormEvent {
        CloseEvent(TimeslotForm source) {
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