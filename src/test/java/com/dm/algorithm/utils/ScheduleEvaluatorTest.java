package com.dm.algorithm.utils;

import com.dm.algorithm.constraint.GroupConflictConstraint;
import com.dm.algorithm.constraint.HardConstraint;
import com.dm.algorithm.constraint.RoomConflictConstraint;
import com.dm.algorithm.constraint.TeacherConflictConstraint;
import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.FitnessResult;
import com.dm.algorithm.model.Gene;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class ScheduleEvaluatorTest {

    private ScheduleEvaluator evaluator;

    @BeforeEach
    void setUp() {
        // Manually assemble the evaluator with actual constraints for testing
        List<HardConstraint> constraints = Arrays.asList(
                new TeacherConflictConstraint(),
                new RoomConflictConstraint(),
                new GroupConflictConstraint());
        evaluator = new ScheduleEvaluator(constraints);
    }

    @Test
    void testTeacherConflict() {
        // Teacher "T1" has two classes in timeslot 0 -> 1 conflict
        Gene g1 = new Gene("C1", "T1", "R1", "G1", 0);
        Gene g2 = new Gene("C2", "T1", "R2", "G2", 0);

        Chromosome c = new Chromosome(Arrays.asList(g1, g2));

        FitnessResult result = evaluator.evaluate(c);

        Assertions.assertEquals(1, result.getConflicts());
        Assertions.assertTrue(result.getFitnessScore() < 1.0);
    }

    @Test
    void testNoConflict() {
        // Different timeslots -> 0 conflicts
        Gene g1 = new Gene("C1", "T1", "R1", "G1", 0);
        Gene g2 = new Gene("C2", "T1", "R2", "G2", 1);

        Chromosome c = new Chromosome(Arrays.asList(g1, g2));

        FitnessResult result = evaluator.evaluate(c);

        Assertions.assertEquals(0, result.getConflicts());
        Assertions.assertEquals(1.0, result.getFitnessScore(), 0.001);
    }

    @Test
    void testRoomConflict() {
        // Room "R1" used twice in timeslot 5
        Gene g1 = new Gene("C1", "T1", "R1", "G1", 5);
        Gene g2 = new Gene("C2", "T2", "R1", "G2", 5);

        Chromosome c = new Chromosome(Arrays.asList(g1, g2));

        FitnessResult result = evaluator.evaluate(c);

        Assertions.assertEquals(1, result.getConflicts());
    }
}
