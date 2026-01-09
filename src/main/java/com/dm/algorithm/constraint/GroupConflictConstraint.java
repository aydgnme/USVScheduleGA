package com.dm.algorithm.constraint;

import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.Gene;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Component
public class GroupConflictConstraint implements HardConstraint {

    @Override
    public int check(Chromosome chromosome) {
        int conflicts = 0;
        Map<String, Map<Integer, List<Gene>>> groupSchedule = new HashMap<>();

        for (Gene gene : chromosome.getGenes()) {
            if (gene.getStudentGroupId() == null)
                continue;

            groupSchedule
                    .computeIfAbsent(gene.getStudentGroupId(), k -> new HashMap<>())
                    .computeIfAbsent(gene.getTimeslot(), k -> new ArrayList<>())
                    .add(gene);
        }

        for (Map<Integer, List<Gene>> slotMap : groupSchedule.values()) {
            for (List<Gene> genesInSlot : slotMap.values()) {
                if (genesInSlot.size() > 1) {
                    conflicts += (genesInSlot.size() - 1);
                }
            }
        }
        return conflicts;
    }
}
