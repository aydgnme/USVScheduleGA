package com.dm.algorithm.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Population {
    private List<Chromosome> chromosomes;

    public Population() {
        this.chromosomes = new ArrayList<>();
    }

    public Population(int populationSize) {
        this.chromosomes = new ArrayList<>(populationSize);
    }

    /**
     * Sorts the population so that the highest fitness chromosomes are first.
     */
    public void sortByFitness() {
        chromosomes.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
    }

    // Getters and Setters
    public List<Chromosome> getChromosomes() { return chromosomes; }
    public void setChromosomes(List<Chromosome> chromosomes) { this.chromosomes = chromosomes; }

    public Chromosome getFittest(int offset) {
        this.sortByFitness();
        return chromosomes.get(offset);
    }
}