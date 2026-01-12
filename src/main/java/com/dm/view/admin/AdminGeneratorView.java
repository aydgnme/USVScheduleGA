package com.dm.view.admin;

import com.dm.algorithm.service.GeneticAlgorithmService;
import com.dm.dto.GroupDto;
import com.dm.dto.ScheduleEntryDto;
import com.dm.service.GroupService;
import com.dm.view.components.AppCard;
import com.dm.view.components.PageHeader;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Route(value = "admin/generator", layout = MainLayout.class)
@PageTitle("Schedule Generator | USV Schedule")
@RolesAllowed("ADMIN")
public class AdminGeneratorView extends VerticalLayout {

    private final GeneticAlgorithmService gaService;
    private final GroupService groupService;

    private MultiSelectComboBox<GroupDto> groupSelector;
    private IntegerField populationSize;
    private IntegerField maxGenerations;
    private NumberField mutationRate;
    private IntegerField elitismCount;

    private Button runButton;
    private Span statusLabel;
    private Span bestFitnessLabel;
    private Grid<ScheduleEntryDto> resultsGrid;
    private AppCard resultsCard;

    public AdminGeneratorView(GeneticAlgorithmService gaService, GroupService groupService) {
        this.gaService = gaService;
        this.groupService = groupService;

        setSizeFull();
        setSpacing(false);

        add(new PageHeader("Schedule Generator", "Automatic Timetable Optimization"));
        add(createConfigCard());
        add(createResultsCard());
    }
    private void configureResultsGrid() {
        resultsGrid = new Grid<>(ScheduleEntryDto.class, false);
        resultsGrid.addColumn(ScheduleEntryDto::getDayOfWeek).setHeader("Day");
        resultsGrid.addColumn(ScheduleEntryDto::getStartTime).setHeader("Start");
        resultsGrid.addColumn(ScheduleEntryDto::getCourseTitle).setHeader("Course");
        resultsGrid.addColumn(ScheduleEntryDto::getGroupCode).setHeader("Group");
        resultsGrid.addColumn(ScheduleEntryDto::getRoomCode).setHeader("Room");

        // HIGHLIGHT LOGIC: Apply "error-row" class to conflicted rows
        resultsGrid.setClassNameGenerator(item -> item.isConflicted() ? "error-row" : null);
        resultsGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
    }

    private AppCard createConfigCard() {
        AppCard card = new AppCard();

        // 1. Initialize the FormLayout here
        FormLayout form = new FormLayout();

        groupSelector = new MultiSelectComboBox<>("Target Student Groups");
        groupSelector.setItems(groupService.getAll());
        groupSelector.setItemLabelGenerator(GroupDto::getCode);
        groupSelector.setPlaceholder("Select groups to include...");

        // 2. This must come AFTER 'form' is initialized and BEFORE it is returned
        form.setColspan(groupSelector, 2);

        populationSize = new IntegerField("Population Size");
        populationSize.setValue(100);

        maxGenerations = new IntegerField("Max Generations");
        maxGenerations.setValue(500);

        mutationRate = new NumberField("Mutation Rate");
        mutationRate.setValue(0.01);

        elitismCount = new IntegerField("Elitism Count");
        elitismCount.setValue(2);

        // Add all fields to the form
        form.add(groupSelector, populationSize, maxGenerations, mutationRate, elitismCount);

        runButton = new Button("Generate Optimal Schedule", e -> runAlgorithm());
        runButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Add form and button to the card
        card.add(form, runButton);
        return card;
    }
    private AppCard createResultsCard() {
        resultsCard = new AppCard();
        resultsCard.setVisible(false);

        statusLabel = new Span("Ready");
        statusLabel.getStyle().set("font-weight", "bold");
        bestFitnessLabel = new Span("-");

        resultsGrid = new Grid<>(ScheduleEntryDto.class, false);
        resultsGrid.addColumn(ScheduleEntryDto::getDayOfWeek).setHeader("Day").setAutoWidth(true);
        resultsGrid.addColumn(ScheduleEntryDto::getStartTime).setHeader("Start").setAutoWidth(true);
        resultsGrid.addColumn(ScheduleEntryDto::getCourseTitle).setHeader("Course").setFlexGrow(1);
        resultsGrid.addColumn(ScheduleEntryDto::getGroupCode).setHeader("Group").setAutoWidth(true);
        resultsGrid.addColumn(ScheduleEntryDto::getRoomCode).setHeader("Room").setAutoWidth(true);
        resultsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
        resultsGrid.setHeight("400px");

        resultsCard.add(new HorizontalLayout(statusLabel, bestFitnessLabel), resultsGrid);
        return resultsCard;
    }

    private void runAlgorithm() {
        if (groupSelector.getValue().isEmpty()) {
            Notification.show("Select groups first", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        runButton.setEnabled(false);
        resultsCard.setVisible(true);
        statusLabel.setText("Running Algorithm...");

        List<Long> groupIds = groupSelector.getValue().stream().map(GroupDto::getId).toList();
        gaService.setPopulationSize(populationSize.getValue());
        gaService.setMutationRate(mutationRate.getValue());
        gaService.setElitismCount(elitismCount.getValue());

        new Thread(() -> {
            try {
                var result = gaService.runFullGeneration(maxGenerations.getValue(), groupIds);
                getUI().ifPresent(ui -> ui.access(() -> {
                    statusLabel.setText("Done!");
                    bestFitnessLabel.setText("Fitness: " + String.format("%.4f", result.fitness()));
                    resultsGrid.setItems(result.entries());
                    runButton.setEnabled(true);
                }));
            } catch (Exception e) {
                getUI().ifPresent(ui -> ui.access(() -> {
                    statusLabel.setText("Error occurred.");
                    runButton.setEnabled(true);
                }));
            }
        }).start();
    }
}