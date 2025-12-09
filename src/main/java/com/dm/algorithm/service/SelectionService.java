package com.dm.algorithm.service;

import com.dm.algorithm.model.Chromosome;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Handles parent selection (e.g., tournament or roulette wheel).
 *
 * TODO: Implement selection strategies.
 */
@Service
public class SelectionService {

    public List<Chromosome> selectParents(List<Chromosome> population) {
        // TODO: implement selection logic
        return population.subList(0, 2);
    }
}