package com.dm.algorithm.service;

import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.Population;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Service
public class SelectionService {

    private int tournamentSize = 5;

    public Chromosome selectParent(Population population) {
        List<Chromosome> tournament = new ArrayList<>();
        List<Chromosome> candidates = new ArrayList<>(population.getChromosomes());

        Collections.shuffle(candidates);
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(candidates.get(i));
        }

        // Return the fittest from the tournament
        return tournament.stream()
                .max((c1, c2) -> Double.compare(c1.getFitness(), c2.getFitness()))
                .orElse(candidates.get(0));
    }

    public int getTournamentSize() { return tournamentSize; }
    public void setTournamentSize(int tournamentSize) { this.tournamentSize = tournamentSize; }
}