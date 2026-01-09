package com.dm.algorithm.service;

import com.dm.algorithm.model.*;
import com.dm.algorithm.utils.ScheduleEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneticAlgorithmService {

    @Autowired
    private SelectionService selectionService;
    @Autowired
    private CrossoverService crossoverService;
    @Autowired
    private MutationService mutationService;
    @Autowired
    private ScheduleEvaluator scheduleEvaluator;

    private double mutationRate = 0.01;
    private int populationSize = 100;
    private int elitismCount = 2; // Keep the top 2 candidates unchanged

    public Population evolve(Population population, int totalTimeslots, List<String> roomIds) {
        Population nextGeneration = new Population(populationSize);

        // 1. Elitism: Keep the best performers
        population.sortByFitness();
        for (int i = 0; i < elitismCount; i++) {
            nextGeneration.getChromosomes().add(population.getChromosomes().get(i));
        }

        // 2. Crossover & Mutation
        while (nextGeneration.getChromosomes().size() < populationSize) {
            Chromosome p1 = selectionService.selectParent(population);
            Chromosome p2 = selectionService.selectParent(population);

            Chromosome child = crossoverService.crossover(p1, p2);
            mutationService.mutate(child, mutationRate, totalTimeslots, roomIds);

            nextGeneration.getChromosomes().add(child);
        }

        // 3. Evaluate New Generation
        for (Chromosome c : nextGeneration.getChromosomes()) {
            FitnessResult res = scheduleEvaluator.evaluate(c);
            c.setFitness(res.getFitnessScore());
        }

        return nextGeneration;
    }

    // Getters and Setters for Configuration
    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getElitismCount() {
        return elitismCount;
    }

    public void setElitismCount(int elitismCount) {
        this.elitismCount = elitismCount;
    }
}