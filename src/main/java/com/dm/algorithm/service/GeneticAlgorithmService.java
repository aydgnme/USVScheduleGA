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

import com.dm.data.entity.ScheduleBackupEntity;
import com.dm.data.entity.ScheduleEntryEntity;
import com.dm.data.repository.ScheduleBackupRepository;
import com.dm.data.repository.ScheduleEntryRepository;

@Service
public class GeneticAlgorithmService {

    @Autowired
    private SelectionService selectionService;
    @Autowired
    private CrossoverService crossoverService;
    @Autowired
    private MutationService mutationService;
    @Autowired
    private CourseOfferingService offeringService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private TimeslotService timeslotService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleEvaluator scheduleEvaluator;
    @Autowired
    private ScheduleEntryRepository scheduleEntryRepository;
    @Autowired
    private ScheduleBackupRepository scheduleBackupRepository;

    private double mutationRate = 0.01;
    private int populationSize = 100;
    private int elitismCount = 2;
    private final Random random = new Random();

    public record GenerationResult(double fitness, List<ScheduleEntryDto> entries) {
    }

    @Transactional
    public List<GenerationResult> runMultipleSolutions(int generations, List<Long> groupIds, int numSolutions,
            java.util.Set<java.time.DayOfWeek> allowedDays) {
        List<GenerationResult> results = new ArrayList<>();

        // Pre-fetch data once to avoid repeated DB hits
        List<CourseOfferingDto> offerings = offeringService.getAll().stream()
                .filter(o -> groupIds.contains(o.getGroupId())).toList();

        if (offerings.isEmpty())
            throw new RuntimeException("No assignments found for selected groups.");

        List<RoomDto> rooms = roomService.getAll();
        List<TimeslotDto> timeslots = timeslotService.getAll().stream()
                .filter(ts -> allowedDays.contains(ts.getDayOfWeek()))
                .toList();

        if (timeslots.isEmpty()) {
            throw new RuntimeException("No timeslots available for the selected days.");
        }

        for (int n = 0; n < numSolutions; n++) {
            Population pop = initializePopulation(offerings, rooms, timeslots);
            List<String> roomIds = rooms.stream().map(r -> r.getId().toString()).toList();

            for (int i = 0; i < generations; i++) {
                pop = evolve(pop, timeslots, roomIds);
            }

            pop.sortByFitness();
            Chromosome best = pop.getChromosomes().get(0);

            // Convert to DTOs but DO NOT save yet
            List<ScheduleEntryDto> entries = convertToDto(best, timeslots, offerings, rooms);
            results.add(new GenerationResult(best.getFitness(), entries));
        }

        // Sort results by fitness (descending, assuming higher is better or adapting to
        // fitness definition)
        // In this system fitness seems to be 1.0 = perfect.
        results.sort((a, b) -> Double.compare(b.fitness(), a.fitness()));

        return results;
    }

    /**
     * Legacy method for backward compatibility if needed, or simple single-run
     * execution.
     */
    @Transactional
    public GenerationResult runFullGeneration(int generations, List<Long> groupIds) {
        // Backup existing schedules for these groups before generating new ones
        backupScheduleForGroups(groupIds);

        java.util.Set<java.time.DayOfWeek> allDays = java.util.EnumSet.allOf(java.time.DayOfWeek.class);
        GenerationResult result = runMultipleSolutions(generations, groupIds, 1, allDays).get(0);
        saveSolution(result.entries());
        return result;
    }

    public void backupScheduleForGroups(List<Long> groupIds) {
        List<ScheduleEntryEntity> entriesToBackup = new ArrayList<>();
        // Find all entries for the selected groups
        for (Long groupId : groupIds) {
            entriesToBackup.addAll(scheduleEntryRepository.findAllByOfferingGroupId(groupId));
        }

        if (!entriesToBackup.isEmpty()) {
            List<ScheduleBackupEntity> backups = entriesToBackup.stream()
                    .map(ScheduleBackupEntity::new)
                    .toList();
            scheduleBackupRepository.saveAll(backups);
            scheduleEntryRepository.deleteAll(entriesToBackup);
        }
    }

    @Transactional
    public void saveSolution(List<ScheduleEntryDto> entries) {
        for (ScheduleEntryDto entry : entries) {
            // Ensure status and other defaults are set if missing, though they should be
            // set in convertToDto
            entry.setStatus(ScheduleStatus.PLANNED);
            scheduleService.save(entry);
        }
    }

    private Population initializePopulation(List<CourseOfferingDto> offerings, List<RoomDto> rooms,
            List<TimeslotDto> timeslots) {
        List<String> roomIds = rooms.stream().map(r -> r.getId().toString()).toList();
        Population population = new Population(populationSize);

        for (int i = 0; i < populationSize; i++) {
            List<Gene> genes = new ArrayList<>();
            for (CourseOfferingDto off : offerings) {
                int slotsNeeded = (off.getWeeklyHours() + 1) / 2;
                for (int s = 0; s < slotsNeeded; s++) {
                    int timeslotIndex = random.nextInt(timeslots.size());
                    genes.add(new Gene(
                            off.getId().toString(),
                            off.getTeacherId().toString(),
                            roomIds.get(random.nextInt(roomIds.size())),
                            off.getGroupId().toString(),
                            timeslotIndex,
                            timeslots.get(timeslotIndex).getDayOfWeek(),
                            off.getType()));
                }
            }
            population.getChromosomes().add(new Chromosome(genes));
        }
        return population;
    }

    private List<ScheduleEntryDto> convertToDto(Chromosome best, List<TimeslotDto> timeslots,
            List<CourseOfferingDto> offerings, List<RoomDto> rooms) {
        List<ScheduleEntryDto> dtos = new ArrayList<>();

        // Create maps for fast lookup
        java.util.Map<String, CourseOfferingDto> offeringMap = offerings.stream()
                .collect(java.util.stream.Collectors.toMap(o -> o.getId().toString(), o -> o));
        java.util.Map<String, RoomDto> roomMap = rooms.stream()
                .collect(java.util.stream.Collectors.toMap(r -> r.getId().toString(), r -> r));

        for (Gene gene : best.getGenes()) {
            int tsIndex = gene.getTimeslot();
            if (tsIndex >= 0 && tsIndex < timeslots.size()) {
                ScheduleEntryDto dto = new ScheduleEntryDto();
                dto.setOfferingId(Long.parseLong(gene.getCourseId()));
                dto.setRoomId(Long.parseLong(gene.getRoomId()));

                TimeslotDto ts = timeslots.get(tsIndex);
                dto.setTimeslotId(ts.getId());
                dto.setStatus(ScheduleStatus.PLANNED);
                dto.setWeekPattern(WeekParity.BOTH);

                // Hydrate view fields
                CourseOfferingDto off = offeringMap.get(gene.getCourseId());
                if (off != null) {
                    dto.setCourseCode(off.getCourseCode());
                    dto.setCourseTitle(off.getCourseTitle());
                    dto.setGroupCode(off.getGroupCode());
                    dto.setTeacherFirstName(off.getTeacherFirstName());
                    dto.setTeacherLastName(off.getTeacherLastName());
                }

                RoomDto room = roomMap.get(gene.getRoomId());
                if (room != null) {
                    dto.setRoomCode(room.getCode());
                }

                dto.setDayOfWeek(ts.getDayOfWeek());
                dto.setStartTime(ts.getStartTime());
                dto.setEndTime(ts.getEndTime());

                // We assume these are transient DTOs until saved
                dtos.add(dto);
            }
        }
        return dtos;
    }

    public Population evolve(Population population, List<TimeslotDto> timeslots, List<String> roomIds) {
        Population nextGeneration = new Population(populationSize);
        population.sortByFitness();
        for (int i = 0; i < elitismCount; i++) {
            nextGeneration.getChromosomes().add(population.getChromosomes().get(i));
        }
        while (nextGeneration.getChromosomes().size() < populationSize) {
            Chromosome p1 = selectionService.selectParent(population);
            Chromosome p2 = selectionService.selectParent(population);
            Chromosome child = crossoverService.crossover(p1, p2);
            mutationService.mutate(child, mutationRate, timeslots, roomIds);
            nextGeneration.getChromosomes().add(child);
        }
        for (Chromosome c : nextGeneration.getChromosomes()) {
            FitnessResult res = scheduleEvaluator.evaluate(c);
            c.setFitness(res.getFitnessScore());
        }
        return nextGeneration;
    }

    // Configuration Getters/Setters
    public void setPopulationSize(int size) {
        this.populationSize = size;
    }

    public void setMutationRate(double rate) {
        this.mutationRate = rate;
    }

    public void setElitismCount(int count) {
        this.elitismCount = count;
    }
}