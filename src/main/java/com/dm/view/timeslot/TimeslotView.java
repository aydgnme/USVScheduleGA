package com.dm.view.timeslot;

import com.dm.dto.TimeslotDto;
import com.dm.service.TimeslotService;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "timeslots", layout = MainLayout.class)
@PageTitle("Timeslots | USV Schedule GA")
@PermitAll
public class TimeslotView extends VerticalLayout {

    private final TimeslotService timeslotService;
    Grid<TimeslotDto> grid = new Grid<>(TimeslotDto.class);
    TimeslotForm form;


    public TimeslotView(TimeslotService timeslotService) {
        this.timeslotService = timeslotService;
        addClassName("timeslot-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new TimeslotForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveTimeslot);
        form.addDeleteListener(this::deleteTimeslot);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveTimeslot(TimeslotForm.SaveEvent event) {
        timeslotService.save(event.getTimeslot());
        updateList();
        closeEditor();
    }

    private void deleteTimeslot(TimeslotForm.DeleteEvent event) {
        timeslotService.delete(event.getTimeslot());
        updateList();
        closeEditor();
    }


    private void configureGrid() {
        grid.addClassName("timeslot-grid");
        grid.setSizeFull();
        grid.setColumns("day", "startTime", "endTime");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editTimeslot(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        Button newButton = new Button("New Timeslot");
        newButton.addClickListener(e -> createTimeslot());

        HorizontalLayout toolbar = new HorizontalLayout(newButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editTimeslot(TimeslotDto timeslot) {
        if (timeslot == null) {
            closeEditor();
        } else {
            form.setTimeslot(timeslot);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setTimeslot(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void createTimeslot() {
        grid.asSingleSelect().clear();
        editTimeslot(new TimeslotDto(null, null, null, null));
    }

    private void updateList() {
        grid.setItems(timeslotService.getAll());
    }
}
