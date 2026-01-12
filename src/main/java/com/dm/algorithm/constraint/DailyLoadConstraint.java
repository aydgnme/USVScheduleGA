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

        // GroupId -> DayIndex (1=Mon, 7=Sun) -> Count
        Map<String, Map<Integer, Integer>> dailyCounts = new HashMap<>();

        for (Gene gene : chromosome.getGenes()) {
            if (gene.getDayOfWeek() == null)
                continue; // Should not happen if initialized correctly

            int dayIndex = gene.getDayOfWeek().getValue();
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