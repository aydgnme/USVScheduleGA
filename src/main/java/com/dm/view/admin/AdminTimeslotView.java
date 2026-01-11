package com.dm.view.admin;

import com.dm.dto.TimeslotDto;
import com.dm.service.TimeslotService;
import com.dm.view.components.AppCard;
import com.dm.view.components.PageHeader;
import com.dm.view.components.SearchToolbar;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

@Route(value = "admin/timeslots", layout = MainLayout.class)
@PageTitle("Timeslot Management | USV Schedule")
@RolesAllowed("ROLE_ADMIN")
public class AdminTimeslotView extends VerticalLayout {

    private final TimeslotService timeslotService;

    private Grid<TimeslotDto> grid;
    private ComboBox<DayOfWeek> dayOfWeek;
    private TimePicker startTime;
    private TimePicker endTime;

    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private Binder<TimeslotDto> binder;

    private TimeslotDto currentTimeslot;

    public AdminTimeslotView(TimeslotService timeslotService) {
        this.timeslotService = timeslotService;

        addClassName("admin-timeslots-view");
        setSpacing(false);
        setSizeFull();

        PageHeader header = new PageHeader("Timeslot Management");

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(40);

        createGrid(splitLayout);
        createEditor(splitLayout);

        add(header, splitLayout);

        refreshGrid();
        clearForm();
    }

    private void createGrid(SplitLayout splitLayout) {
        AppCard card = new AppCard();
        card.setHeightFull();

        SearchToolbar toolbar = new SearchToolbar("Search timeslots...", this::onSearch);
        Button addButton = new Button("New Timeslot", e -> clearForm());
        toolbar.addPrimaryAction(addButton);

        grid = new Grid<>(TimeslotDto.class, false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();

        grid.addColumn(t -> t.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()))
                .setHeader("Day").setSortable(true).setFlexGrow(1);
        grid.addColumn(TimeslotDto::getStartTime).setHeader("Start").setSortable(true).setAutoWidth(true);
        grid.addColumn(TimeslotDto::getEndTime).setHeader("End").setSortable(true).setAutoWidth(true);

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                populateForm(e.getValue());
            } else {
                clearForm();
            }
        });

        card.add(toolbar, grid);
        splitLayout.addToPrimary(card);
    }

    private void createEditor(SplitLayout splitLayout) {
        AppCard card = new AppCard();
        card.setHeightFull();

        FormLayout formLayout = new FormLayout();

        dayOfWeek = new ComboBox<>("Day of Week");
        dayOfWeek.setItems(DayOfWeek.values());
        dayOfWeek.setItemLabelGenerator(d -> d.getDisplayName(TextStyle.FULL, Locale.getDefault()));

        startTime = new TimePicker("Start Time");
        endTime = new TimePicker("End Time");

        formLayout.add(dayOfWeek, startTime, endTime);

        binder = new BeanValidationBinder<>(TimeslotDto.class);
        binder.bindInstanceFields(this);

        saveButton = new Button("Save", e -> save());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancelButton = new Button("Cancel", e -> clearForm());

        deleteButton = new Button("Delete", e -> delete());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.setVisible(false);

        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        actions.setSpacing(true);

        card.add(formLayout, actions);
        splitLayout.addToSecondary(card);
    }

    private void onSearch(String searchTerm) {
        // Simple search by day name
        grid.setItems(timeslotService.getAll().stream()
                .filter(t -> matches(t, searchTerm))
                .toList());
    }

    private boolean matches(TimeslotDto t, String term) {
        if (term == null || term.isEmpty())
            return true;
        String lower = term.toLowerCase();
        return t.getDayOfWeek().toString().toLowerCase().contains(lower);
    }

    private void refreshGrid() {
        grid.setItems(timeslotService.getAll());
    }

    private void populateForm(TimeslotDto value) {
        this.currentTimeslot = value;
        binder.readBean(value);
        deleteButton.setVisible(true);
    }

    private void clearForm() {
        this.currentTimeslot = null;
        binder.readBean(new TimeslotDto());
        grid.asSingleSelect().clear();
        deleteButton.setVisible(false);
    }

    private void save() {
        TimeslotDto dto = new TimeslotDto();
        if (this.currentTimeslot != null) {
            dto = this.currentTimeslot;
        }

        try {
            if (binder.writeBeanIfValid(dto)) {
                timeslotService.save(dto);
                refreshGrid();
                clearForm();
                Notification.show("Timeslot saved successfully")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        } catch (Exception e) {
            Notification.show("Error saving: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void delete() {
        if (currentTimeslot == null)
            return;
        try {
            timeslotService.delete(currentTimeslot.getId());
            refreshGrid();
            clearForm();
            Notification.show("Timeslot deleted")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Cannot delete: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
