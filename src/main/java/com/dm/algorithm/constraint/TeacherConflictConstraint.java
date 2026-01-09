package com.dm.algorithm.constraint;

import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.Gene;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Component
public class TeacherConflictConstraint implements HardConstraint {

    @Override
    public int check(Chromosome chromosome) {
        int conflicts = 0;
        Map<String, Map<Integer, List<Gene>>> teacherSchedule = new HashMap<>();

        for (Gene gene : chromosome.getGenes()) {
            teacherSchedule
                    .computeIfAbsent(gene.getTeacherId(), k -> new HashMap<>())
                    .computeIfAbsent(gene.getTimeslot(), k -> new ArrayList<>())
                    .add(gene);
        }

        for (Map<Integer, List<Gene>> slotMap : teacherSchedule.values()) {
            for (List<Gene> genesInSlot : slotMap.values()) {
                if (genesInSlot.size() > 1) {
                    conflicts += (genesInSlot.size() - 1);
                }
            }
        }
        return conflicts;
    }
}
