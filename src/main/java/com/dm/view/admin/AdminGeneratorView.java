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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Route(value = "admin/generator", layout = MainLayout.class)
@PageTitle("Schedule Generator | USV Schedule")
@RolesAllowed("ADMIN")
public class AdminGeneratorView extends VerticalLayout {

    private final GeneticAlgorithmService gaService;
    private final GroupService groupService;

    private MultiSelectComboBox<GroupDto> groupSelector;
    private MultiSelectComboBox<java.time.DayOfWeek> allowedDaysSelector;
    private IntegerField populationSize;
    private IntegerField maxGenerations;
    private NumberField mutationRate;
    private IntegerField elitismCount;
    private IntegerField numSolutions;

    private Button runButton;
    private Tabs solutionTabs;
    private Button applyButton;
    private Span statusLabel;
    private Span bestFitnessLabel;
    private Grid<ScheduleEntryDto> resultsGrid;
    private AppCard resultsCard;

    private List<GeneticAlgorithmService.GenerationResult> currentResults;

    public AdminGeneratorView(GeneticAlgorithmService gaService, GroupService groupService) {
        this.gaService = gaService;
        this.groupService = groupService;

        setSizeFull();
        setSpacing(false);

        add(new PageHeader("Schedule Generator", "Automatic Timetable Optimization"));
        add(createConfigCard());
        add(createResultsCard());
    }

    private AppCard createConfigCard() {
        AppCard card = new AppCard();

        // 1. Initialize the FormLayout here
        FormLayout form = new FormLayout();

        groupSelector = new MultiSelectComboBox<>("Target Student Groups");
        groupSelector.setItems(groupService.getAll());
        groupSelector.setItemLabelGenerator(GroupDto::getCode);
        groupSelector.setPlaceholder("Select groups to include...");

        allowedDaysSelector = new MultiSelectComboBox<>("Teaching Days");
        allowedDaysSelector.setItems(java.time.DayOfWeek.values());
        allowedDaysSelector.setValue(java.util.Set.of(
                java.time.DayOfWeek.MONDAY,
                java.time.DayOfWeek.TUESDAY,
                java.time.DayOfWeek.WEDNESDAY,
                java.time.DayOfWeek.THURSDAY,
                java.time.DayOfWeek.FRIDAY));
        allowedDaysSelector.setPlaceholder("Select allowed days...");

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

        numSolutions = new IntegerField("Number of Solutions");
        numSolutions.setValue(3);
        numSolutions.setMin(1);
        numSolutions.setMax(5);

        // Add all fields to the form
        form.add(groupSelector, allowedDaysSelector, populationSize, maxGenerations, mutationRate, elitismCount,
                numSolutions);

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

        resultsCard.add(new HorizontalLayout(statusLabel, bestFitnessLabel));

        solutionTabs = new Tabs();
        solutionTabs.addSelectedChangeListener(e -> {
            int index = solutionTabs.getSelectedIndex();
            if (index >= 0 && currentResults != null && index < currentResults.size()) {
                updateResultView(currentResults.get(index));
            }
        });
        resultsCard.add(solutionTabs);

        resultsCard.add(resultsGrid);

        applyButton = new Button("Apply This Schedule", e -> applyCurrentSchedule());
        applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        applyButton.setEnabled(false);
        resultsCard.add(applyButton);

        return resultsCard;
    }

    private void updateResultView(GeneticAlgorithmService.GenerationResult result) {
        bestFitnessLabel.setText("Fitness Score: " + String.format("%.4f", result.fitness()));
        resultsGrid.setItems(result.entries());
    }

    private void applyCurrentSchedule() {
        int index = solutionTabs.getSelectedIndex();
        if (index >= 0 && currentResults != null && index < currentResults.size()) {
            GeneticAlgorithmService.GenerationResult selected = currentResults.get(index);
            gaService.saveSolution(selected.entries());
            Notification.show("Schedule saved successfully!", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            applyButton.setEnabled(false);
            statusLabel.setText("Saved");
        }
    }

    private void runAlgorithm() {
        if (groupSelector.getValue().isEmpty()) {
            Notification.show("Select groups first", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (allowedDaysSelector.getValue().isEmpty()) {
            Notification.show("Select at least one teaching day", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        runButton.setEnabled(false);
        resultsCard.setVisible(true);
        statusLabel.setText("Running Algorithm...");

        List<Long> groupIds = groupSelector.getValue().stream().map(GroupDto::getId).toList();
        java.util.Set<java.time.DayOfWeek> allowedDays = allowedDaysSelector.getValue();

        gaService.setPopulationSize(populationSize.getValue());
        gaService.setMutationRate(mutationRate.getValue());
        gaService.setElitismCount(elitismCount.getValue());
        int solutionsToRun = numSolutions.getValue();
        int generations = maxGenerations.getValue();

        long startTime = System.currentTimeMillis();

        new Thread(() -> {
            try {
                // Return List<GenerationResult> now
                var results = gaService.runMultipleSolutions(generations, groupIds, solutionsToRun, allowedDays);
                long duration = System.currentTimeMillis() - startTime;

                getUI().ifPresent(ui -> ui.access(() -> {
                    currentResults = results;
                    statusLabel.setText("Done! (" + duration + " ms)");
                    Notification
                            .show("Generation complete! Found " + results.size() + " solutions in " + duration + " ms.",
                                    3000,
                                    Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                    solutionTabs.removeAll();
                    for (int i = 0; i < results.size(); i++) {
                        Tab tab = new Tab("Option " + (i + 1));
                        solutionTabs.add(tab);
                    }

                    if (!results.isEmpty()) {
                        solutionTabs.setSelectedIndex(0);
                        updateResultView(results.get(0));
                    }

                    applyButton.setEnabled(true);
                    runButton.setEnabled(true);
                }));
            } catch (Exception e) {
                e.printStackTrace();
                getUI().ifPresent(ui -> ui.access(() -> {
                    statusLabel.setText("Error occurred: " + e.getMessage());
                    runButton.setEnabled(true);
                }));
            }
        }).start();
    }
}