package com.dm.algorithm.utils;

import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.FitnessResult;
import com.dm.algorithm.model.Gene;
import java.util.List;

public class ScheduleEvaluator {

    public static FitnessResult evaluate(Chromosome chromosome) {
        int conflicts = 0;
        List<Gene> genes = chromosome.getGenes();

        for (int i = 0; i < genes.size(); i++) {
            for (int j = i + 1; j < genes.size(); j++) {
                Gene g1 = genes.get(i);
                Gene g2 = genes.get(j);

                // If they are in the same timeslot, check for collisions
                if (g1.getTimeslot() == g2.getTimeslot()) {
                    // 1. Room Conflict
                    if (g1.getRoomId().equals(g2.getRoomId())) conflicts++;

                    // 2. Teacher Conflict
                    if (g1.getTeacherId().equals(g2.getTeacherId())) conflicts++;

                    // 3. Student Group Conflict
                    if (g1.getStudentGroupId().equals(g2.getStudentGroupId())) conflicts++;
                }
            }
        }

        // Fitness score is between 0 and 1. 1.0 means 0 conflicts.
        double fitnessScore = 1.0 / (1.0 + conflicts);
        return new FitnessResult(fitnessScore, conflicts);
    }
}