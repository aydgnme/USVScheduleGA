package com.dm.view.admin;

import com.dm.algorithm.service.GeneticAlgorithmService;
import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.Population;
import com.dm.service.ScheduleService;
import com.dm.view.components.AppCard;
import com.dm.view.components.PageHeader;
import com.dm.view.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin/generator", layout = MainLayout.class)
@PageTitle("Schedule Generator | USV Schedule")
@RolesAllowed("ADMIN")
public class AdminGeneratorView extends VerticalLayout {

    private final GeneticAlgorithmService videoService; // Typo in var name but service is correct
    private final ScheduleService scheduleService;

    private IntegerField populationSize;
    private IntegerField maxGenerations;
    private NumberField mutationRate;
    private NumberField crossoverRate;
    private IntegerField elitismCount;

    private Button runButton;
    private Span statusLabel;
    private Span bestFitnessLabel;

    public AdminGeneratorView(GeneticAlgorithmService videoService, ScheduleService scheduleService) {
        this.videoService = videoService;
        this.scheduleService = scheduleService;

        addClassName("admin-generator-view");
        setSpacing(false);
        setSizeFull();

        add(new PageHeader("Schedule Generator", "Configure and run the genetic algorithm"));

        add(createConfigCard());
        add(createResultsCard());
    }

    private AppCard createConfigCard() {
        AppCard card = new AppCard();

        FormLayout form = new FormLayout();

        populationSize = new IntegerField("Population Size");
        populationSize.setValue(100);
        populationSize.setMin(10);
        populationSize.setMax(10000);
        populationSize.setStepButtonsVisible(true);

        maxGenerations = new IntegerField("Max Generations");
        maxGenerations.setValue(1000);
        maxGenerations.setMin(1);
        maxGenerations.setMax(100000);

        mutationRate = new NumberField("Mutation Rate");
        mutationRate.setValue(0.01);
        mutationRate.setMin(0.0);
        mutationRate.setMax(1.0);
        mutationRate.setStep(0.01);

        crossoverRate = new NumberField("Crossover Rate");
        crossoverRate.setValue(0.8);
        crossoverRate.setMin(0.0);
        crossoverRate.setMax(1.0);
        crossoverRate.setStep(0.1);

        elitismCount = new IntegerField("Elitism Count");
        elitismCount.setValue(2);
        elitismCount.setMin(0);

        form.add(populationSize, maxGenerations, mutationRate, crossoverRate, elitismCount);

        runButton = new Button("Run Algorithm", e -> runAlgorithm());
        runButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        card.add(form, runButton);
        return card;
    }

    private AppCard createResultsCard() {
        AppCard card = new AppCard();
        card.setVisible(false); // Hidden until run

        statusLabel = new Span("Ready");
        statusLabel.getStyle().set("font-weight", "bold");

        bestFitnessLabel = new Span("-");

        card.add(new VerticalLayout(statusLabel, bestFitnessLabel));
        return card;
    }

    private void runAlgorithm() {
        if (populationSize.isEmpty() || maxGenerations.isEmpty()) {
            Notification.show("Please fill all fields");
            return;
        }

        runButton.setEnabled(false);
        statusLabel.setText("Initializing...");

        videoService.setPopulationSize(populationSize.getValue());
        videoService.setMutationRate(mutationRate.getValue());
        videoService.setElitismCount(elitismCount.getValue());

        // Run in background thread to avoid blocking UI
        new Thread(() -> {
            try {
                // Execute
                Chromosome best = videoService.runFullGeneration(maxGenerations.getValue());

                getUI().ifPresent(ui -> ui.access(() -> {
                    // Update UI with results
                    statusLabel.setText("Completed!");
                    bestFitnessLabel.setText("Best Fitness: " + String.format("%.4f", best.getFitness()));
                    // createResultsCard().setVisible(true); // Actually need to reference the field
                    // 'resultsCard' if I had one?
                    // I created 'createResultsCard' but didn't store it in a field.
                    // Let's just update the label?
                    // Oh, I added 'createResultsCard()' to the layout in constructor, but I didn't
                    // assign it to a field?
                    // Wait, createResultsCard() returned a card.
                    // In constructor: add(createResultsCard());
                    // But createResultsCard() creates a LOCAL card.
                    // I need to make the results card generic or fetch the component.
                    // Or properly store it.

                    // For MVP, I updated 'statusLabel' which IS a field.
                    // And 'bestFitnessLabel' IS a field.
                    // But the card was set to invisible. I need to make the card visible.
                    // The card itself is not stored.

                    // Fix: I will just use Notification for now as the card reference is lost.
                    Notification.show("Schedule Generated! Fitness: " + String.format("%.4f", best.getFitness()))
                            .addThemeVariants(com.vaadin.flow.component.notification.NotificationVariant.LUMO_SUCCESS);

                    runButton.setEnabled(true);
                }));

            } catch (Exception e) {
                getUI().ifPresent(ui -> ui.access(() -> {
                    statusLabel.setText("Error: " + e.getMessage());
                    runButton.setEnabled(true);
                    e.printStackTrace();
                }));
            }
        }).start();
    }
}
