package com.dm.algorithm.service;

import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.Gene;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class MutationService {

    private final Random random = new Random();

    public void mutate(Chromosome chromosome, double mutationRate, List<com.dm.dto.TimeslotDto> timeslots,
            List<String> roomIds) {
        int totalTimeslots = timeslots.size();
        for (Gene gene : chromosome.getGenes()) {
            if (Math.random() < mutationRate) {
                // Randomly change either the timeslot or the room
                if (random.nextBoolean()) {
                    int newTimeslotIndex = random.nextInt(totalTimeslots);
                    gene.setTimeslot(newTimeslotIndex);
                    // CRITICAL FIX: Update the DayOfWeek as well!
                    gene.setDayOfWeek(timeslots.get(newTimeslotIndex).getDayOfWeek());
                } else {
                    gene.setRoomId(roomIds.get(random.nextInt(roomIds.size())));
                }
            }
        }
    }
}