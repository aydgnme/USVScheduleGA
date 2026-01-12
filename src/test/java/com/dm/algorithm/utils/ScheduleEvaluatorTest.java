package com.dm.algorithm.utils;

import com.dm.algorithm.constraint.GroupConflictConstraint;
import com.dm.algorithm.constraint.HardConstraint;
import com.dm.algorithm.constraint.RoomConflictConstraint;
import com.dm.algorithm.constraint.TeacherConflictConstraint;
import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.FitnessResult;
import com.dm.algorithm.model.Gene;
import com.dm.model.types.ActivityType; // ADD THIS IMPORT
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class ScheduleEvaluatorTest {

    private ScheduleEvaluator evaluator;

    @BeforeEach
    void setUp() {
        List<HardConstraint> constraints = Arrays.asList(
                new TeacherConflictConstraint(),
                new RoomConflictConstraint(),
                new GroupConflictConstraint());
        evaluator = new ScheduleEvaluator(constraints);
    }

    @Test
    void testTeacherConflict() {
        // ADD ActivityType.LAB as the 6th argument
        Gene g1 = new Gene("C1", "T1", "R1", "G1", 0, ActivityType.LAB);
        Gene g2 = new Gene("C2", "T1", "R2", "G2", 0, ActivityType.LAB);

        Chromosome c = new Chromosome(Arrays.asList(g1, g2));
        FitnessResult result = evaluator.evaluate(c);

        Assertions.assertEquals(1, result.getConflicts());
    }

    @Test
    void testNoConflict() {
        // ADD ActivityType.LAB as the 6th argument
        Gene g1 = new Gene("C1", "T1", "R1", "G1", 0, ActivityType.LAB);
        Gene g2 = new Gene("C2", "T1", "R2", "G2", 1, ActivityType.LAB);

        Chromosome c = new Chromosome(Arrays.asList(g1, g2));
        FitnessResult result = evaluator.evaluate(c);

        Assertions.assertEquals(0, result.getConflicts());
    }

    @Test
    void testRoomConflict() {
        // ADD ActivityType.LAB as the 6th argument
        Gene g1 = new Gene("C1", "T1", "R1", "G1", 5, ActivityType.LAB);
        Gene g2 = new Gene("C2", "T2", "R1", "G2", 5, ActivityType.LAB);

        Chromosome c = new Chromosome(Arrays.asList(g1, g2));
        FitnessResult result = evaluator.evaluate(c);

        Assertions.assertEquals(1, result.getConflicts());
    }
}