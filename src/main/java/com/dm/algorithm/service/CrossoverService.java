package com.dm.algorithm.service;

import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.Gene;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CrossoverService {

    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        List<Gene> childGenes = new ArrayList<>();

        for (int i = 0; i < parent1.getGenes().size(); i++) {
            // 50% chance to take gene from parent 1 or parent 2
            if (Math.random() < 0.5) {
                childGenes.add(copyGene(parent1.getGenes().get(i)));
            } else {
                childGenes.add(copyGene(parent2.getGenes().get(i)));
            }
        }
        return new Chromosome(childGenes);
    }

    private Gene copyGene(Gene gene) {
        return new Gene(gene.getCourseId(), gene.getTeacherId(),
                gene.getRoomId(), gene.getStudentGroupId(), gene.getTimeslot());
    }
}