package com.dm.algorithm.service;

import com.dm.algorithm.model.*;
import com.dm.algorithm.utils.ScheduleEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneticAlgorithmService {

    @Autowired
    private SelectionService selectionService;
    @Autowired
    private CrossoverService crossoverService;
    @Autowired
    private MutationService mutationService;
    @Autowired
    private com.dm.service.CourseOfferingService offeringService;
    @Autowired
    private com.dm.service.RoomService roomService;
    @Autowired
    private com.dm.service.TimeslotService timeslotService;
    @Autowired
    private com.dm.service.ScheduleService scheduleService;
    @Autowired
    private ScheduleEvaluator scheduleEvaluator;

    private double mutationRate = 0.01;
    private int populationSize = 100;
    private int elitismCount = 2; // Keep the top 2 candidates unchanged

    // ... existing fields ...

    public Population initializePopulationFromDB() {
        List<com.dm.dto.CourseOfferingDto> offerings = offeringService.getAll();
        List<com.dm.dto.RoomDto> rooms = roomService.getAll();
        List<com.dm.dto.TimeslotDto> timeslots = timeslotService.getAll();

        if (offerings.isEmpty() || rooms.isEmpty() || timeslots.isEmpty()) {
            throw new RuntimeException("Insufficient data (offerings/rooms/timeslots) to initialize");
        }

        int totalTimeslots = timeslots.size();
        List<String> roomIds = rooms.stream().map(r -> r.getId().toString())
                .collect(java.util.stream.Collectors.toList());

        Population population = new Population(populationSize);

        for (int i = 0; i < populationSize; i++) {
            List<Gene> genes = new ArrayList<>();
            for (com.dm.dto.CourseOfferingDto off : offerings) {
                // If offering has N hours, we might need N genes if we schedule them
                // independently.
                // For MVP, if WeeklyHours=2, we create 2 genes?
                // Or simplified: 1 Gene = 1 Block.
                // If the system allows split blocks, we need multiple genes.
                // Let's assume 1 Gene per Hour for simplicity, or 1 Gene per Offering if it's a
                // block.
                // Given the fields "startHour, endHour" in DB... wait.
                // ScheduleEntry has Timeslot. Timeslot is 1 block (usually 2 hours).
                // So if WeeklyHours=2, it's 1 Timeslot. WeeklyHours=4 -> 2 Timeslots.
                // We'll create (Hours / 2) genes? Or just (Hours) genes?
                // Standard TS is 2 hours.
                int slotsNeeded = (off.getWeeklyHours() + 1) / 2; // ceil(hours/2) approx
                if (slotsNeeded < 1)
                    slotsNeeded = 1;

                for (int s = 0; s < slotsNeeded; s++) {
                    int randomSlot = (int) (Math.random() * totalTimeslots);
                    String randomRoom = roomIds.get((int) (Math.random() * roomIds.size()));

                    Gene gene = new Gene(
                            off.getId().toString(), // Storing OfferingID as CourseID for tracking
                            off.getTeacherId().toString(),
                            randomRoom,
                            off.getGroupId().toString(),
                            randomSlot);
                    genes.add(gene);
                }
            }
            population.getChromosomes().add(new Chromosome(genes));
        }
        return population;
    }

    public void saveSchedule(Chromosome best) {
        // Clear existing planned schedule? Or just append?
        // Usually we want to overwrite 'PLANNED' entries.
        // For MVP, let's just save.

        List<com.dm.dto.TimeslotDto> timeslots = timeslotService.getAll(); // Need mapping index -> ID

        for (Gene gene : best.getGenes()) {
            Long offeringId = Long.parseLong(gene.getCourseId());
            Long roomId = Long.parseLong(gene.getRoomId());

            // Map int timeslot index back to TimeslotDto
            int tsIndex = gene.getTimeslot();
            if (tsIndex >= 0 && tsIndex < timeslots.size()) {
                Long timeslotId = timeslots.get(tsIndex).getId();

                com.dm.dto.ScheduleEntryDto dto = new com.dm.dto.ScheduleEntryDto();
                dto.setOfferingId(offeringId);
                dto.setRoomId(roomId);
                dto.setTimeslotId(timeslotId);
                dto.setStatus(com.dm.model.types.ScheduleStatus.PLANNED);
                dto.setWeekPattern(com.dm.model.types.WeekParity.BOTH); // Default

                scheduleService.save(dto);
            }
        }
    }

    public Chromosome runFullGeneration(int generations) {
        Population pop = initializePopulationFromDB();

        // Fetch context for evolve
        List<com.dm.dto.TimeslotDto> timeslots = timeslotService.getAll();
        int totalTimeslots = timeslots.size();
        List<com.dm.dto.RoomDto> rooms = roomService.getAll();
        List<String> roomIds = rooms.stream().map(r -> r.getId().toString())
                .collect(java.util.stream.Collectors.toList());

        for (int i = 0; i < generations; i++) {
            pop = evolve(pop, totalTimeslots, roomIds);
        }

        pop.sortByFitness();
        Chromosome best = pop.getChromosomes().get(0);
        saveSchedule(best);
        return best;
    }

    public Population evolve(Population population, int totalTimeslots, List<String> roomIds) {
        Population nextGeneration = new Population(populationSize);

        // 1. Elitism: Keep the best performers
        population.sortByFitness();
        for (int i = 0; i < elitismCount; i++) {
            nextGeneration.getChromosomes().add(population.getChromosomes().get(i));
        }

        // 2. Crossover & Mutation
        while (nextGeneration.getChromosomes().size() < populationSize) {
            Chromosome p1 = selectionService.selectParent(population);
            Chromosome p2 = selectionService.selectParent(population);

            Chromosome child = crossoverService.crossover(p1, p2);
            mutationService.mutate(child, mutationRate, totalTimeslots, roomIds);

            nextGeneration.getChromosomes().add(child);
        }

        // 3. Evaluate New Generation
        for (Chromosome c : nextGeneration.getChromosomes()) {
            FitnessResult res = scheduleEvaluator.evaluate(c);
            c.setFitness(res.getFitnessScore());
        }

        return nextGeneration;
    }

    // Getters and Setters for Configuration
    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getElitismCount() {
        return elitismCount;
    }

    public void setElitismCount(int elitismCount) {
        this.elitismCount = elitismCount;
    }
}