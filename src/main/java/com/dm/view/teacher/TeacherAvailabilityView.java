package com.dm.view.teacher;

import com.dm.dto.TeacherPreferenceDto;
import com.dm.data.entity.UserEntity;
import com.dm.data.repository.UserRepository;
import com.dm.model.types.PreferenceType;
import com.dm.service.TeacherAvailabilityService;
import com.dm.view.components.AppCard;
import com.dm.view.components.PageHeader;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.DayOfWeek;
import java.util.*;

@Route(value = "teacher/availability", layout = MainLayout.class)
@PageTitle("My Availability | USV Schedule")
@RolesAllowed("ROLE_TEACHER")
public class TeacherAvailabilityView extends VerticalLayout {

    private final TeacherAvailabilityService service;
    private final UserRepository userRepository;

    // Grid state: Key = "MONDAY-8", Value = DTO
    private Map<String, TeacherPreferenceDto> preferenceMap = new HashMap<>();
    private Long currentTeacherId;

    public TeacherAvailabilityView(TeacherAvailabilityService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;

        addClassName("teacher-availability-view");
        setHeightFull();
        setSpacing(false);

        add(new PageHeader("My Availability", "Define your preferred and unavailable timeslots"));

        determineCurrentTeacher();
        createGrid();
    }

    private void determineCurrentTeacher() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = "";
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user != null && user.getTeacherProfile() != null) {
            this.currentTeacherId = user.getTeacherProfile().getId();
        } else {
            Notification.show("Error: Teacher profile not found for user " + email);
        }
    }

    private void createGrid() {
        if (currentTeacherId == null)
            return;

        // Load existing
        List<TeacherPreferenceDto> existing = service.getPreferences(currentTeacherId);
        for (TeacherPreferenceDto dto : existing) {
            // For this MVP, we treat startHour as the block index.
            String key = dto.getDayOfWeek() + "-" + dto.getStartHour();
            preferenceMap.put(key, dto);
        }

        AppCard card = new AppCard();
        card.setHeightFull();
        card.add(createLegend());
        card.add(buildTimeGrid());
        card.add(createActions());

        add(card);
    }

    private HorizontalLayout createLegend() {
        HorizontalLayout l = new HorizontalLayout();
        l.setSpacing(true);
        l.add(createBadge(PreferenceType.NEUTRAL, "Available (Default)"));
        l.add(createBadge(PreferenceType.PREFERRED, "Preferred"));
        l.add(createBadge(PreferenceType.UNAVAILABLE, "Unavailable"));
        return l;
    }

    private Span createBadge(PreferenceType type, String label) {
        Span s = new Span(label);
        s.getElement().getThemeList().add("badge " + getBadgeTheme(type));
        return s;
    }

    private String getBadgeTheme(PreferenceType type) {
        if (type == PreferenceType.UNAVAILABLE)
            return "error";
        if (type == PreferenceType.PREFERRED)
            return "success";
        return "contrast";
    }

    private Div buildTimeGrid() {
        Div grid = new Div();
        grid.getStyle().set("display", "grid");
        grid.getStyle().set("grid-template-columns", "80px repeat(5, 1fr)"); // Time + 5 Days
        grid.getStyle().set("gap", "4px");
        grid.getStyle().set("overflow-y", "auto");
        grid.setHeightFull();

        // Header Row
        grid.add(new Div()); // Corner
        for (DayOfWeek day : EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)) {
            Div header = new Div(new Span(day.name().substring(0, 3)));
            header.getStyle().set("font-weight", "bold").set("text-align", "center");
            grid.add(header);
        }

        // Rows 8 to 20
        for (int hour = 8; hour < 20; hour++) {
            // Time Label
            Div timeLabel = new Div(new Span(String.format("%02d:00", hour)));
            timeLabel.getStyle().set("text-align", "right").set("padding-right", "10px").set("color",
                    "var(--lumo-secondary-text-color)");
            grid.add(timeLabel);

            // Cells
            for (DayOfWeek day : EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)) {
                grid.add(createCell(day, hour));
            }
        }
        return grid;
    }

    private Div createCell(DayOfWeek day, int hour) {
        String key = day + "-" + hour;
        TeacherPreferenceDto dto = preferenceMap.getOrDefault(key, null);
        PreferenceType currentType = (dto != null && dto.getType() != null) ? dto.getType() : PreferenceType.NEUTRAL;

        Div cell = new Div();
        cell.getStyle().set("border", "1px solid var(--lumo-contrast-10pct)");
        cell.getStyle().set("border-radius", "4px");
        cell.getStyle().set("height", "40px");
        cell.getStyle().set("cursor", "pointer");
        cell.getStyle().set("display", "flex");
        cell.getStyle().set("align-items", "center");
        cell.getStyle().set("justify-content", "center");

        applyStyle(cell, currentType);

        cell.addClickListener(e -> {
            PreferenceType next = getNext(getModelType(cell));
            updateMap(key, day, hour, next);
            applyStyle(cell, next);
        });

        return cell;
    }

    // Helper to get type from cell model (state management in UI)
    private PreferenceType getModelType(Div cell) {
        PreferenceType type = com.vaadin.flow.component.ComponentUtil.getData(cell, PreferenceType.class);
        return type != null ? type : PreferenceType.NEUTRAL;
    }

    private void applyStyle(Div cell, PreferenceType type) {
        cell.removeAll();
        com.vaadin.flow.component.ComponentUtil.setData(cell, PreferenceType.class, type);

        if (type == PreferenceType.UNAVAILABLE) {
            cell.getStyle().set("background-color", "var(--lumo-error-color-10pct)");
            cell.add(VaadinIcon.BAN.create());
        } else if (type == PreferenceType.PREFERRED) {
            cell.getStyle().set("background-color", "var(--lumo-success-color-10pct)");
            cell.add(VaadinIcon.STAR.create());
        } else {
            cell.getStyle().set("background-color", "transparent");
        }
    }

    private PreferenceType getNext(PreferenceType current) {
        if (current == PreferenceType.NEUTRAL)
            return PreferenceType.PREFERRED;
        if (current == PreferenceType.PREFERRED)
            return PreferenceType.UNAVAILABLE;
        return PreferenceType.NEUTRAL;
    }

    private void updateMap(String key, DayOfWeek day, int hour, PreferenceType type) {
        TeacherPreferenceDto dto = preferenceMap.get(key);
        if (dto == null) {
            dto = new TeacherPreferenceDto();
            dto.setTeacherId(currentTeacherId);
            dto.setDayOfWeek(day);
            dto.setStartHour(hour);
            dto.setEndHour(hour + 1);
        }
        dto.setType(type);
        preferenceMap.put(key, dto);
    }

    private HorizontalLayout createActions() {
        Button save = new Button("Save Changes", e -> save());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return new HorizontalLayout(save);
    }

    private void save() {
        try {
            for (TeacherPreferenceDto dto : preferenceMap.values()) {
                if (dto.getType() == PreferenceType.NEUTRAL) {
                    if (dto.getId() != null) {
                        service.deletePreference(dto.getId());
                    }
                } else {
                    service.savePreference(dto);
                }
            }
            Notification.show("Availability updated successfully")
                    .addThemeVariants(com.vaadin.flow.component.notification.NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Error saving: " + e.getMessage())
                    .addThemeVariants(com.vaadin.flow.component.notification.NotificationVariant.LUMO_ERROR);
        }
    }
}
