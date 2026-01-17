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
                new GroupConflictConstraint(),
                new com.dm.algorithm.constraint.DailyLoadConstraint());
        evaluator = new ScheduleEvaluator(constraints);
    }

    @Test
    void testTeacherConflict() {
        Gene g1 = new Gene("C1", "T1", "R1", "G1", 0, java.time.DayOfWeek.MONDAY, ActivityType.LAB);
        Gene g2 = new Gene("C2", "T1", "R2", "G2", 0, java.time.DayOfWeek.MONDAY, ActivityType.LAB);

        Chromosome c = new Chromosome(Arrays.asList(g1, g2));
        FitnessResult result = evaluator.evaluate(c);

        Assertions.assertEquals(1, result.getConflicts());
    }

    @Test
    void testNoConflict() {
        Gene g1 = new Gene("C1", "T1", "R1", "G1", 0, java.time.DayOfWeek.MONDAY, ActivityType.LAB);
        Gene g2 = new Gene("C2", "T1", "R2", "G2", 1, java.time.DayOfWeek.MONDAY, ActivityType.LAB);

        Chromosome c = new Chromosome(Arrays.asList(g1, g2));
        FitnessResult result = evaluator.evaluate(c);

        Assertions.assertEquals(0, result.getConflicts());
    }

    @Test
    void testRoomConflict() {
        Gene g1 = new Gene("C1", "T1", "R1", "G1", 5, java.time.DayOfWeek.MONDAY, ActivityType.LAB);
        Gene g2 = new Gene("C2", "T2", "R1", "G2", 5, java.time.DayOfWeek.MONDAY, ActivityType.LAB);

        Chromosome c = new Chromosome(Arrays.asList(g1, g2));
        FitnessResult result = evaluator.evaluate(c);

        Assertions.assertEquals(1, result.getConflicts());
    }

    @Test
    void testDailyLoadConflict() {
        // Create 5 genes for the same group (G1) on the same day (MONDAY)
        List<Gene> genes = new java.util.ArrayList<>();
        for (int i = 0; i < 5; i++) {
            // Distinct teachers and rooms to distinguish from other conflicts
            genes.add(new Gene("C" + i, "T" + i, "R" + i, "G1", i, java.time.DayOfWeek.MONDAY, ActivityType.LECTURE));
        }

        Chromosome c = new Chromosome(genes);
        FitnessResult result = evaluator.evaluate(c);

        // Should have 0 hard conflicts from overlaps (all different times, rooms,
        // teachers)
        // But DailyLoadConstraint should see 5 activities on MONDAY
        // Limit is 4, so 5 - 4 = 1 violation.
        Assertions.assertEquals(1, result.getConflicts());
    }

    @Test
    void testGroupConflict_Lectures() {
        // Two LECTUREs for same group (G1) at same time (slot 0)
        // Previously this might have been allowed, now should be a conflict
        Gene g1 = new Gene("C1", "T1", "R1", "G1", 0, java.time.DayOfWeek.MONDAY, ActivityType.LECTURE);
        Gene g2 = new Gene("C2", "T2", "R2", "G1", 0, java.time.DayOfWeek.MONDAY, ActivityType.LECTURE);

        Chromosome c = new Chromosome(Arrays.asList(g1, g2));
        FitnessResult result = evaluator.evaluate(c);

        // 2 activities in same slot => 1 conflict
        Assertions.assertEquals(1, result.getConflicts());
    }
}