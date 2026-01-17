package com.dm.view.teacher.components;

import com.dm.dto.CourseDto;
import com.dm.dto.GroupDto;
import com.dm.service.CourseService;
import com.dm.service.GroupService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

import java.time.DayOfWeek;
import java.util.List;

public class TeacherPreferenceForm extends FormLayout {

    ComboBox<DayOfWeek> dayOfWeek = new ComboBox<>("Preferred Day");
    ComboBox<CourseDto> course = new ComboBox<>("Specific Course (Optional)");
    ComboBox<GroupDto> group = new ComboBox<>("Specific Group (Optional)");
    ComboBox<Integer> studyYear = new ComboBox<>("Study Year (Optional)");

    Button save = new Button("Add Preference");
    Button cancel = new Button("Clear");

    public TeacherPreferenceForm(CourseService courseService, GroupService groupService) {
        addClassName("teacher-preference-form");

        dayOfWeek.setItems(DayOfWeek.values());

        List<CourseDto> courses = courseService.findAll();
        course.setItems(courses);
        course.setItemLabelGenerator(c -> c.getCode() + " - " + c.getTitle());
        course.setClearButtonVisible(true);

        List<GroupDto> groups = groupService.getAll();
        group.setItems(groups);
        group.setItemLabelGenerator(GroupDto::getCode);
        group.setClearButtonVisible(true);

        studyYear.setItems(1, 2, 3, 4);
        studyYear.setClearButtonVisible(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        save.addClickListener(event -> validateAndSave());
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        add(dayOfWeek, course, group, studyYear, buttons);
    }

    private void validateAndSave() {
        if (dayOfWeek.getValue() == null) {
            return;
        }
        fireEvent(new SaveEvent(this, dayOfWeek.getValue(), course.getValue(), group.getValue(), studyYear.getValue()));
    }

    // Events
    public static abstract class TeacherPreferenceFormEvent extends ComponentEvent<TeacherPreferenceForm> {
        private final DayOfWeek day;
        private final CourseDto course;
        private final GroupDto group;
        private final Integer studyYear;

        protected TeacherPreferenceFormEvent(TeacherPreferenceForm source, DayOfWeek day, CourseDto course,
                GroupDto group, Integer studyYear) {
            super(source, false);
            this.day = day;
            this.course = course;
            this.group = group;
            this.studyYear = studyYear;
        }

        public DayOfWeek getDay() {
            return day;
        }

        public CourseDto getCourse() {
            return course;
        }

        public GroupDto getGroup() {
            return group;
        }

        public Integer getStudyYear() {
            return studyYear;
        }
    }

    public static class SaveEvent extends TeacherPreferenceFormEvent {
        SaveEvent(TeacherPreferenceForm source, DayOfWeek day, CourseDto course, GroupDto group, Integer studyYear) {
            super(source, day, course, group, studyYear);
        }
    }

    public static class CloseEvent extends TeacherPreferenceFormEvent {
        CloseEvent(TeacherPreferenceForm source) {
            super(source, null, null, null, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
