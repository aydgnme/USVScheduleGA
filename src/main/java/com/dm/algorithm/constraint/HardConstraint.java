package com.dm.algorithm.constraint;

import com.dm.algorithm.model.Chromosome;

/**
 * Interface for hard constraints.
 * Violation of a hard constraint usually invalidates the schedule
 * or incurs a massive penalty.
 */
public interface HardConstraint {
    /**
     * Checks if the constraint works on a single chromosome.
     * 
     * @return number of conflicts found
     */
    int check(Chromosome chromosome);
}
