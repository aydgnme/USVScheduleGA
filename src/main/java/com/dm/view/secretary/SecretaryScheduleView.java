package com.dm.view.secretary;

import com.dm.dto.CourseOfferingDto;
import com.dm.dto.GroupDto;
import com.dm.dto.RoomDto;
import com.dm.dto.ScheduleEntryDto;
import com.dm.dto.TimeslotDto;
import com.dm.model.types.ScheduleStatus;
import com.dm.model.types.WeekParity;
import com.dm.service.CourseOfferingService;
import com.dm.service.GroupService;
import com.dm.service.RoomService;
import com.dm.service.ScheduleService;
import com.dm.service.TimeslotService;
import com.dm.view.components.AppCard;
import com.dm.view.components.PageHeader;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Route(value = "secretary/schedule", layout = MainLayout.class)
@PageTitle("Manual Scheduling | USV Schedule")
@RolesAllowed({ "ROLE_SECRETARY", "ROLE_ADMIN" })
public class SecretaryScheduleView extends VerticalLayout {

    private final ScheduleService scheduleService;
    private final TimeslotService timeslotService;
    private final GroupService groupService;
    private final CourseOfferingService offeringService;
    private final RoomService roomService;

    private ComboBox<GroupDto> groupWrapper;
    private Grid<TimeslotDto> scheduleGrid;

    // Cache for current group's entries
    private List<ScheduleEntryDto> currentEntries;

    public SecretaryScheduleView(ScheduleService scheduleService,
            TimeslotService timeslotService,
            GroupService groupService,
            CourseOfferingService offeringService,
            RoomService roomService) {
        this.scheduleService = scheduleService;
        this.timeslotService = timeslotService;
        this.groupService = groupService;
        this.offeringService = offeringService;
        this.roomService = roomService;

        addClassName("secretary-schedule-view");
        setSpacing(false);
        setSizeFull();

        PageHeader header = new PageHeader("Manual Scheduling");

        AppCard card = new AppCard();
        card.setHeightFull();

        createToolbar(card);
        createTimetable(card);

        add(header, card);
    }

    private void createToolbar(AppCard card) {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.setPadding(true);
        toolbar.setAlignItems(Alignment.CENTER);

        groupWrapper = new ComboBox<>("Select Group");
        groupWrapper.setItems(groupService.getAll());
        groupWrapper.setItemLabelGenerator(GroupDto::getCode);
        groupWrapper.addValueChangeListener(e -> refreshTimetable());
        groupWrapper.setWidth("250px");

        toolbar.add(groupWrapper);
        card.add(toolbar);
    }

    private void createTimetable(AppCard card) {
        scheduleGrid = new Grid<>(TimeslotDto.class, false);
        scheduleGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
        scheduleGrid.setHeightFull();

        // Column 1: Time Range
        scheduleGrid.addColumn(t -> t.getStartTime() + " - " + t.getEndTime())
                .setHeader("Time")
                .setAutoWidth(true)
                .setFlexGrow(0);

        // Columns for Days needed (Monday to Friday usu.)
        // We assume timeslots are generic ranges, but TimeslotDto has DayOfWeek...
        // Wait. TimeslotDto has DayOfWeek.
        // So a "Timeslot" entity is specific to a day: "Monday 8-10".
        // My Grid Row model is broken if I list TimeslotDto which are unique per day.

        // Correction: If Timeslot is (Day, Start, End), then the grid should likely
        // show:
        // Rows: Distinct Time Ranges (e.g. 8-10, 10-12).
        // Cols: Days (Mon, Tue...).
        // Cell content: The Timeslot matching (Row Time + Col Day) AND the
        // ScheduleEntry in it.

        // So I need unique Time Ranges.

        // But for simplicity/MVP, maybe I just list ALL Timeslots (sorted by Day then
        // Time) as Rows?
        // No, that's a list view, not a timetable.

        // Better: Rows = 8-10, 10-12, etc. (assuming synchronization).
        // Cols = Mon, Tue, Wed...

        // I will dynamically fetch distinct TimeRanges.

        // BUT, retrieving Timeslots from DB gives me (Day, Start, End).
        // I'll group them by StartTime.

        List<TimeslotDto> allSlots = timeslotService.getAll();
        List<TimeslotDto> distinctTimes = allSlots.stream()
                .filter(t -> t.getDayOfWeek() == DayOfWeek.MONDAY) // Assume Mon has all standard slots
                .sorted(Comparator.comparing(TimeslotDto::getStartTime))
                .collect(Collectors.toList());

        if (distinctTimes.isEmpty() && !allSlots.isEmpty()) {
            // Fallback if no Monday slots, just take unique start times
            distinctTimes = allSlots.stream()
                    .collect(Collectors.toMap(TimeslotDto::getStartTime, Function.identity(), (a, b) -> a))
                    .values().stream()
                    .sorted(Comparator.comparing(TimeslotDto::getStartTime))
                    .collect(Collectors.toList());
        }

        scheduleGrid.setItems(distinctTimes);

        for (DayOfWeek day : DayOfWeek.values()) {
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY)
                continue; // Skip weekend for now unless needed

            scheduleGrid.addColumn(new ComponentRenderer<>(slotPrototype -> {
                // Find actual timeslot for this Day + Prototype Start/End
                TimeslotDto actualSlot = findSlot(day, slotPrototype.getStartTime());

                Div cell = new Div();
                cell.getStyle().set("width", "100%");
                cell.getStyle().set("height", "100%");
                cell.getStyle().set("min-height", "50px");
                cell.getStyle().set("cursor", "pointer");
                cell.addClassName("timetable-cell"); // For potential CSS styles

                if (actualSlot == null) {
                    cell.setText("-");
                    cell.getStyle().set("background-color", "#f0f0f0"); // N/A
                    return cell;
                }

                // Find entries for this slot
                List<ScheduleEntryDto> entries = findEntries(actualSlot.getId());

                if (entries.isEmpty()) {
                    cell.setText("Empty");
                    cell.getStyle().set("color", "#ccc");
                } else {
                    for (ScheduleEntryDto entry : entries) {
                        Div badge = new Div();
                        badge.setText(entry.getCourseCode() + " (" + entry.getRoomCode() + ")");
                        badge.getStyle().set("background-color", "#e3f2fd");
                        badge.getStyle().set("border-radius", "4px");
                        badge.getStyle().set("padding", "2px 4px");
                        badge.getStyle().set("margin-bottom", "2px");
                        badge.getStyle().set("font-size", "0.85em");
                        cell.add(badge);
                    }
                }

                cell.addClickListener(e -> openCellDialog(actualSlot, entries));

                return cell;
            })).setHeader(day.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
        }

        card.add(scheduleGrid);
    }

    private TimeslotDto findSlot(DayOfWeek day, java.time.LocalTime start) {
        return timeslotService.getAll().stream()
                .filter(t -> t.getDayOfWeek() == day && t.getStartTime().equals(start))
                .findFirst().orElse(null);
    }

    private List<ScheduleEntryDto> findEntries(Long timeslotId) {
        if (currentEntries == null)
            return List.of();
        return currentEntries.stream()
                .filter(e -> e.getTimeslotId().equals(timeslotId))
                .collect(Collectors.toList());
    }

    private void refreshTimetable() {
        if (groupWrapper.getValue() != null) {
            currentEntries = scheduleService.getByGroupId(groupWrapper.getValue().getId());
        } else {
            currentEntries = List.of();
        }
        scheduleGrid.getDataProvider().refreshAll();
    }

    private void openCellDialog(TimeslotDto slot, List<ScheduleEntryDto> existing) {
        if (groupWrapper.getValue() == null) {
            Notification.show("Please select a group first");
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Manage Slot: " + slot.getDayOfWeek() + " " + slot.getStartTime());

        VerticalLayout content = new VerticalLayout();

        // Show existing (allow delete)
        if (!existing.isEmpty()) {
            existing.forEach(entry -> {
                HorizontalLayout row = new HorizontalLayout();
                row.setAlignItems(Alignment.CENTER);
                Span label = new Span(entry.getCourseTitle() + " in " + entry.getRoomCode());
                Button del = new Button("Remove", e -> {
                    scheduleService.delete(entry.getId());
                    refreshTimetable();
                    dialog.close(); // Refresh behavior simplistic
                });
                del.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
                row.add(label, del);
                content.add(row);
            });
            content.add(new com.vaadin.flow.component.html.Hr());
        }

        // Add new
        ComboBox<CourseOfferingDto> off = new ComboBox<>("Course Assignment");
        off.setItems(offeringService.getByGroupId(groupWrapper.getValue().getId()));
        off.setItemLabelGenerator(o -> "ID:" + o.getId() + " (Hrs:" + o.getWeeklyHours() + ")"); // Need lookup course
                                                                                                 // name from service or
                                                                                                 // DTO details
        // DTO details (courseCode/Title) aren't in CourseOfferingDto directly by
        // default,
        // need to check CourseOfferingDto again.
        // If not, might need a cache map like in previous view.

        // Let's assume for now just ID/Hours, refactor later for names if needed.

        ComboBox<RoomDto> room = new ComboBox<>("Room");
        room.setItems(roomService.getAll());
        room.setItemLabelGenerator(r -> r.getCode());

        ComboBox<WeekParity> par = new ComboBox<>("Parity");
        par.setItems(WeekParity.values());
        par.setValue(WeekParity.BOTH);

        Button save = new Button("Assign", e -> {
            if (off.getValue() != null && room.getValue() != null) {
                ScheduleEntryDto dto = new ScheduleEntryDto();
                dto.setOfferingId(off.getValue().getId());
                dto.setTimeslotId(slot.getId());
                dto.setRoomId(room.getValue().getId());
                dto.setWeekPattern(par.getValue());
                dto.setStatus(ScheduleStatus.PLANNED);

                try {
                    scheduleService.save(dto);
                    refreshTimetable();
                    dialog.close();
                    Notification.show("Saved").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } catch (Exception ex) {
                    Notification.show("Error: " + ex.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        content.add(off, room, par, save);
        dialog.add(content);

        dialog.open();
    }
}
