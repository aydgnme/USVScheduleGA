package com.dm.algorithm.service;

import com.dm.algorithm.model.Chromosome;
import org.springframework.stereotype.Service;

/**
 * Handles mutation of Chromosomes (introducing diversity).
 *
 * TODO: Implement random swaps, room/time shifts, etc.
 */
@Service
public class MutationService {

    public Chromosome mutate(Chromosome chromosome) {
        // TODO: implement mutation
        return chromosome;
    }
}