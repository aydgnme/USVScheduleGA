package com.dm.algorithm.constraint;

import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.Gene;
import com.dm.model.types.ActivityType;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class GroupConflictConstraint implements HardConstraint {

    @Override
    public int check(Chromosome chromosome) {
        int conflicts = 0;
        Map<String, Map<Integer, List<Gene>>> groupSchedule = new HashMap<>();

        for (Gene gene : chromosome.getGenes()) {
            groupSchedule
                    .computeIfAbsent(gene.getStudentGroupId(), k -> new HashMap<>())
                    .computeIfAbsent(gene.getTimeslot(), k -> new ArrayList<>())
                    .add(gene);
        }

        for (Map<Integer, List<Gene>> slotMap : groupSchedule.values()) {
            for (List<Gene> genesInSlot : slotMap.values()) {
                if (genesInSlot.size() > 1) {
                    // Check if any activity in this group's slot is a LAB or SEMINAR
                    // If everything is a LECTURE, overlap is allowed (0 conflicts)
                    boolean containsStrictActivity = genesInSlot.stream()
                            .anyMatch(g -> g.getActivityType() != ActivityType.LECTURE);

                    if (containsStrictActivity) {
                        conflicts += (genesInSlot.size() - 1);
                    }
                }
            }
        }
        return conflicts;
    }
}