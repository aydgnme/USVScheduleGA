
package com.dm.view.room;

import com.dm.dto.RoomDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class RoomForm extends FormLayout {
    Binder<RoomDto> binder = new BeanValidationBinder<>(RoomDto.class);

    TextField code = new TextField("Code");
    TextField type = new TextField("Type");
    IntegerField capacity = new IntegerField("Capacity");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public RoomForm() {
        addClassName("room-form");
        binder.bindInstanceFields(this);

        add(code,
                type,
                capacity,
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


    public void setRoom(RoomDto room) {
        binder.setBean(room);
    }

    // Events
    public static abstract class RoomFormEvent extends ComponentEvent<RoomForm> {
        private final RoomDto room;

        protected RoomFormEvent(RoomForm source, RoomDto room) {
            super(source, false);
            this.room = room;
        }

        public RoomDto getRoom() {
            return room;
        }
    }

    public static class SaveEvent extends RoomFormEvent {
        SaveEvent(RoomForm source, RoomDto room) {
            super(source, room);
        }
    }

    public static class DeleteEvent extends RoomFormEvent {
        DeleteEvent(RoomForm source, RoomDto room) {
            super(source, room);
        }

    }

    public static class CloseEvent extends RoomFormEvent {
        CloseEvent(RoomForm source) {
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
