package com.dm.algorithm.constraint;

import com.dm.algorithm.model.Chromosome;

/**
 * Interface for soft constraints.
 * Violation of a soft constraint incurs a smaller penalty but solution is
 * valid.
 */
public interface SoftConstraint {
    /**
     * Checks how much the soft constraint is violated.
     * 
     * @return penalty score
     */
    double check(Chromosome chromosome);
}
