package com.dm.algorithm.service;

import com.dm.algorithm.model.Chromosome;
import com.dm.algorithm.model.Gene;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class MutationService {

    private final Random random = new Random();

    public void mutate(Chromosome chromosome, double mutationRate, int totalTimeslots, List<String> roomIds) {
        for (Gene gene : chromosome.getGenes()) {
            if (Math.random() < mutationRate) {
                // Randomly change either the timeslot or the room
                if (random.nextBoolean()) {
                    gene.setTimeslot(random.nextInt(totalTimeslots));
                } else {
                    gene.setRoomId(roomIds.get(random.nextInt(roomIds.size())));
                }
            }
        }
    }
}