package com.dm.algorithm.utils;

import com.dm.algorithm.constraint.HardConstraint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ContextTest {

    @Autowired
    private ScheduleEvaluator scheduleEvaluator;

    @Autowired
    private List<HardConstraint> hardConstraints;

    @Test
    public void testConstraintsAreLoaded() {
        Assertions.assertNotNull(scheduleEvaluator, "ScheduleEvaluator should be loaded");
        Assertions.assertNotNull(hardConstraints, "HardConstraints list should be loaded");
        Assertions.assertFalse(hardConstraints.isEmpty(), "HardConstraints list should not be empty");

        System.out.println("Loaded constraints: " + hardConstraints.size());
        hardConstraints.forEach(c -> System.out.println("Constraint: " + c.getClass().getSimpleName()));
    }
}
