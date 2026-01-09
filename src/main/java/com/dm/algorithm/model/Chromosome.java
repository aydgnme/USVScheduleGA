package com.dm.algorithm.model;

import java.util.ArrayList;
import java.util.List;

public class Chromosome {
    private List<Gene> genes;
    private double fitness;

    public Chromosome() {
        this.genes = new ArrayList<>();
        this.fitness = 0.0;
    }

    public Chromosome(List<Gene> genes) {
        this.genes = genes;
        this.fitness = 0.0;
    }

    // Getters and Setters
    public List<Gene> getGenes() { return genes; }
    public void setGenes(List<Gene> genes) { this.genes = genes; }

    public double getFitness() { return fitness; }
    public void setFitness(double fitness) { this.fitness = fitness; }
}