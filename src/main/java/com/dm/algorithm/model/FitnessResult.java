package com.dm.algorithm.model;

public class FitnessResult {
    private double fitnessScore;
    private int conflicts;

    public FitnessResult() {}

    public FitnessResult(double fitnessScore, int conflicts) {
        this.fitnessScore = fitnessScore;
        this.conflicts = conflicts;
    }

    // Getters and Setters
    public double getFitnessScore() { return fitnessScore; }
    public void setFitnessScore(double fitnessScore) { this.fitnessScore = fitnessScore; }

    public int getConflicts() { return conflicts; }
    public void setConflicts(int conflicts) { this.conflicts = conflicts; }
}