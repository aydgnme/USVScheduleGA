package com.dm.algorithm.utils;

import com.dm.algorithm.constraint.HardConstraint;
import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.FitnessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleEvaluator {

    private final List<HardConstraint> hardConstraints;

    @Autowired
    public ScheduleEvaluator(List<HardConstraint> hardConstraints) {
        this.hardConstraints = hardConstraints;
    }

    public FitnessResult evaluate(Chromosome chromosome) {
        int hardConflicts = 0;

        for (HardConstraint constraint : hardConstraints) {
            hardConflicts += constraint.check(chromosome);
        }

        // Simple fitness function: 1 / (1 + conflicts)
        double fitness = 1.0 / (1.0 + hardConflicts);

        return new FitnessResult(fitness, hardConflicts);
    }
}