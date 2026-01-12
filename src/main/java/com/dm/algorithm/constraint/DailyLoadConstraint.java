package com.dm.algorithm.constraint;

import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.Gene;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class DailyLoadConstraint implements HardConstraint {

    @Override
    public int check(Chromosome chromosome) {
        int violations = 0;
        // Based on V15 migration, there are 6 slots per day (08:00 to 20:00)
        int SLOTS_PER_DAY = 6;

        // GroupId -> DayIndex -> Count
        Map<String, Map<Integer, Integer>> dailyCounts = new HashMap<>();

        for (Gene gene : chromosome.getGenes()) {
            int dayIndex = gene.getTimeslot() / SLOTS_PER_DAY;
            dailyCounts
                    .computeIfAbsent(gene.getStudentGroupId(), k -> new HashMap<>())
                    .merge(dayIndex, 1, Integer::sum);
        }

        for (Map<Integer, Integer> groupDays : dailyCounts.values()) {
            for (Integer count : groupDays.values()) {
                if (count > 4) {
                    violations += (count - 4);
                }
            }
        }
        return violations;
    }
}