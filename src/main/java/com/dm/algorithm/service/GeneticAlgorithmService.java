package com.dm.algorithm.service;

import com.dm.algorithm.model.*;
import com.dm.algorithm.utils.ScheduleEvaluator;
import com.dm.dto.*;
import com.dm.model.types.ScheduleStatus;
import com.dm.model.types.WeekParity;
import com.dm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class GeneticAlgorithmService {

    @Autowired private SelectionService selectionService;
    @Autowired private CrossoverService crossoverService;
    @Autowired private MutationService mutationService;
    @Autowired private CourseOfferingService offeringService;
    @Autowired private RoomService roomService;
    @Autowired private TimeslotService timeslotService;
    @Autowired private ScheduleService scheduleService;
    @Autowired private ScheduleEvaluator scheduleEvaluator;

    private double mutationRate = 0.01;
    private int populationSize = 100;
    private int elitismCount = 2;
    private final Random random = new Random();

    public record GenerationResult(double fitness, List<ScheduleEntryDto> entries) {}

    @Transactional
    public GenerationResult runFullGeneration(int generations, List<Long> groupIds) {
        Population pop = initializePopulationFromDB(groupIds);

        List<TimeslotDto> timeslots = timeslotService.getAll();
        int totalTimeslots = timeslots.size();
        List<String> roomIds = roomService.getAll().stream().map(r -> r.getId().toString()).toList();

        for (int i = 0; i < generations; i++) {
            pop = evolve(pop, totalTimeslots, roomIds);
        }

        pop.sortByFitness();
        Chromosome best = pop.getChromosomes().get(0);
        List<ScheduleEntryDto> savedEntries = saveSchedule(best);

        return new GenerationResult(best.getFitness(), savedEntries);
    }

    private Population initializePopulationFromDB(List<Long> groupIds) {
        List<CourseOfferingDto> offerings = offeringService.getAll().stream()
                .filter(o -> groupIds.contains(o.getGroupId())).toList();
        List<RoomDto> rooms = roomService.getAll();
        List<TimeslotDto> timeslots = timeslotService.getAll();

        if (offerings.isEmpty()) throw new RuntimeException("No assignments found for selected groups.");

        List<String> roomIds = rooms.stream().map(r -> r.getId().toString()).toList();
        Population population = new Population(populationSize);

        for (int i = 0; i < populationSize; i++) {
            List<Gene> genes = new ArrayList<>();
            for (CourseOfferingDto off : offerings) {
                int slotsNeeded = (off.getWeeklyHours() + 1) / 2;
                for (int s = 0; s < slotsNeeded; s++) {
                    genes.add(new Gene(
                            off.getId().toString(),
                            off.getTeacherId().toString(),
                            roomIds.get(random.nextInt(roomIds.size())),
                            off.getGroupId().toString(),
                            random.nextInt(timeslots.size()),
                            off.getType()
                    ));
                }
            }
            population.getChromosomes().add(new Chromosome(genes));
        }
        return population;
    }

    private List<ScheduleEntryDto> saveSchedule(Chromosome best) {
        List<ScheduleEntryDto> saved = new ArrayList<>();
        List<TimeslotDto> timeslots = timeslotService.getAll();

        for (Gene gene : best.getGenes()) {
            int tsIndex = gene.getTimeslot();
            if (tsIndex >= 0 && tsIndex < timeslots.size()) {
                ScheduleEntryDto dto = new ScheduleEntryDto();
                dto.setOfferingId(Long.parseLong(gene.getCourseId()));
                dto.setRoomId(Long.parseLong(gene.getRoomId()));
                dto.setTimeslotId(timeslots.get(tsIndex).getId());
                dto.setStatus(ScheduleStatus.PLANNED);
                dto.setWeekPattern(WeekParity.BOTH);
                saved.add(scheduleService.save(dto));
            }
        }
        return saved;
    }

    public Population evolve(Population population, int totalTimeslots, List<String> roomIds) {
        Population nextGeneration = new Population(populationSize);
        population.sortByFitness();
        for (int i = 0; i < elitismCount; i++) {
            nextGeneration.getChromosomes().add(population.getChromosomes().get(i));
        }
        while (nextGeneration.getChromosomes().size() < populationSize) {
            Chromosome p1 = selectionService.selectParent(population);
            Chromosome p2 = selectionService.selectParent(population);
            Chromosome child = crossoverService.crossover(p1, p2);
            mutationService.mutate(child, mutationRate, totalTimeslots, roomIds);
            nextGeneration.getChromosomes().add(child);
        }
        for (Chromosome c : nextGeneration.getChromosomes()) {
            FitnessResult res = scheduleEvaluator.evaluate(c);
            c.setFitness(res.getFitnessScore());
        }
        return nextGeneration;
    }

    // Configuration Getters/Setters
    public void setPopulationSize(int size) { this.populationSize = size; }
    public void setMutationRate(double rate) { this.mutationRate = rate; }
    public void setElitismCount(int count) { this.elitismCount = count; }
}