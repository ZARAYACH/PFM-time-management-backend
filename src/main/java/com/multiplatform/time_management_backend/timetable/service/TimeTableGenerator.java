package com.multiplatform.time_management_backend.timetable.service;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.academicclass.repository.AcademicClassRepository;
import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.department.repository.TeacherRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.group.repository.GroupRepository;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.room.service.ClassRoomService;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.timetable.modal.Day;
import com.multiplatform.time_management_backend.timetable.modal.Slot;
import com.multiplatform.time_management_backend.timetable.modal.TimeSlot;
import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import com.multiplatform.time_management_backend.timetable.repository.TimeTableRepository;
import com.multiplatform.time_management_backend.timetable.service.geneticalgorithm.Chromosome;
import com.multiplatform.time_management_backend.timetable.service.geneticalgorithm.Data;
import com.multiplatform.time_management_backend.timetable.service.geneticalgorithm.Generator;
import com.multiplatform.time_management_backend.user.model.Teacher;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTableGenerator {

    private final TimeTableRepository timeTableRepository;
    private final ClassRoomService classRoomService;
    private final AcademicClassRepository academicClassRepository;

    private final static int populationSize = 10;
    private final static int maxGenerations = 10000;
    private final static double mutationRate = 0.5;

    public List<TimeTable> generateTimeTables(Semester semester) throws BadArgumentException {
        Map<Group, Map<DayOfWeek, Day>> groupToTimeTableDto = generateSemesterTimeTables(semester);
        Map<Group, TimeTable> timeTablesPerSemester = timeTableRepository.findAllBySemester(semester)
                .stream().collect(Collectors.toMap(TimeTable::getGroup, o -> o));
        groupToTimeTableDto.forEach((key, value) -> {
            if (timeTablesPerSemester.containsKey(key)) {
                timeTablesPerSemester.get(key).setDays(value);
            } else {
                timeTablesPerSemester.put(key, new TimeTable(null, semester, key, value, null, null));
            }
        });
        return timeTableRepository.saveAllAndFlush(timeTablesPerSemester.values());
    }

    private Map<Group, Map<DayOfWeek, Day>> generateSemesterTimeTables(Semester semester) throws BadArgumentException {
        Data data = createData(semester);
        Generator gen = Generator.getInstance(data);
        gen.initializePopulation(populationSize);

        gen.getPopulation().calculateAllFitness();

//        printPopulation(gen.getPopulation());
        int generationCount = 0;
//        System.out.println("Generation: " + generationCount + " averageFitness: "
//                + gen.getPopulation().getAverageFitness() + " Fittest: " + gen.getPopulation().getFittestScore() + "\n");

        while (gen.getPopulation().getFittestScore() < 1 && generationCount < maxGenerations) {
            generationCount++;
            gen.selection();
            gen.crossover();
            gen.mutation(mutationRate);
            gen.addOffSpring();
            gen.getPopulation().calculateAllFitness();
//            printPopulation(gen.getPopulation());
//            System.out.println("Generation: " + generationCount + " averageFitness: "
//                    + gen.getPopulation().getAverageFitness() + " Fittest: " + gen.getPopulation().getFittestScore());
        }
        return chromosomeToTimeTableDto(gen.getPopulation().getFittestChromosome(), data);
    }

    private Map<Group, Map<DayOfWeek, Day>> chromosomeToTimeTableDto(Chromosome chromosome, Data data) throws BadArgumentException {
        Map<Group, Map<DayOfWeek, Day>> groupTimeTableDtoMap = new HashMap<>();

        for (int i = 0; i < chromosome.getChromosomeLength(); i++) {
            int gene = chromosome.getGene(i);
            AcademicClass cls = data.getClassByID(gene);
            if (cls.getGroup() == null) {
                continue;
            }
            DayOfWeek day = data.getWorkingDays().get((i * chromosome.getData().getDaysPerWeek() / chromosome.getChromosomeLength()) % chromosome.getData().getDaysPerWeek());
            int roomID = Math.toIntExact(data.getRooms()[(i * data.getRooms().length * data.getDaysPerWeek() / chromosome.getChromosomeLength()) % data.getRooms().length].getId());
            int period = i % data.getPeriodsPerDay();
            cls.setAllowedRoom(data.getRoomByID(roomID));
            if (groupTimeTableDtoMap.get(cls.getGroup()) == null) {
                groupTimeTableDtoMap.put(cls.getGroup(), initializeTimeTableDays());
                groupTimeTableDtoMap.get(cls.getGroup())
                        .get(day).timeSlots().put(Slot.get(period), new TimeSlot(cls.getTeacher().getId(), cls.getCourse().getId(), (long) roomID, cls.getGroup().getId()));
            } else {
                groupTimeTableDtoMap.get(cls.getGroup())
                        .get(day).timeSlots().put(Slot.get(period), new TimeSlot(cls.getTeacher().getId(), cls.getCourse().getId(), (long) roomID, cls.getGroup().getId()));
            }
            academicClassRepository.saveAndFlush(cls);
        }
        return groupTimeTableDtoMap;
    }

    private Data createData(Semester semester) {
        List<AcademicClass> academicClasses = academicClassRepository.findAllBySemester(semester);
        Group[] groups = academicClasses.stream().map(AcademicClass::getGroup).toArray(Group[]::new);
        Teacher[] teachers = academicClasses.stream().map(AcademicClass::getTeacher).toArray(Teacher[]::new);
        ClassRoom[] classRooms = classRoomService.list().toArray(ClassRoom[]::new);
        Course[] courses = semester.getAcademicClass().stream().map(AcademicClass::getCourse).toArray(Course[]::new);

        return new Data(semester, groups, teachers, classRooms, courses, 4, getWorkWeekDays(), academicClasses.toArray(AcademicClass[]::new));
    }

    private List<DayOfWeek> getWorkWeekDays() {
        return Arrays.stream(DayOfWeek.values())
                .filter(dayOfWeek -> !dayOfWeek.equals(DayOfWeek.SUNDAY)).toList();
    }

    private Map<DayOfWeek, Day> initializeTimeTableDays() {
        return Map.of(DayOfWeek.MONDAY, new Day(new HashMap<>()),
                DayOfWeek.TUESDAY, new Day(new HashMap<>()),
                DayOfWeek.WEDNESDAY, new Day(new HashMap<>()),
                DayOfWeek.THURSDAY, new Day(new HashMap<>()),
                DayOfWeek.FRIDAY, new Day(new HashMap<>()),
                DayOfWeek.SATURDAY, new Day(new HashMap<>()));
    }

}
