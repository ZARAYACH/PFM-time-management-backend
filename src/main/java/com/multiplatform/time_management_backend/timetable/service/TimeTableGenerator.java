package com.multiplatform.time_management_backend.timetable.service;

import com.multiplatform.time_management_backend.course.model.Course;
import com.multiplatform.time_management_backend.course.model.TeacherCourse;
import com.multiplatform.time_management_backend.course.service.CourseService;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.room.service.ClassRoomService;
import com.multiplatform.time_management_backend.semester.modal.AcademicSemester;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.semester.service.AcademicSemesterService;
import com.multiplatform.time_management_backend.timetable.modal.Day;
import com.multiplatform.time_management_backend.timetable.modal.Slot;
import com.multiplatform.time_management_backend.timetable.modal.TimeSlot;
import com.multiplatform.time_management_backend.timetable.modal.TimeTableDto;
import com.multiplatform.time_management_backend.timetable.repository.TimeTableRepository;
import com.multiplatform.time_management_backend.user.model.Teacher;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTableGenerator {

    private final TimeTableRepository timeTableRepository;
    private final AcademicSemesterService academicSemesterService;
    private final CourseService courseService;
    private final ClassRoomService classRoomService;

    @Transactional
    public TimeTableSet generateTimeTables(Semester semester, int populationSize, int generations) {
        List<TimeTableSet> population = generateInitialPopulation(semester, populationSize);

        for (int gen = 0; gen < generations; gen++) {
            population.sort(Comparator.comparingInt(TimeTableSet::fitness).reversed());

            List<TimeTableSet> newGeneration = new ArrayList<>();

            int eliteCount = (int) (0.1 * population.size());
            newGeneration.addAll(population.subList(0, eliteCount));

            // Generate new individuals
            while (newGeneration.size() < populationSize) {
                TimeTableSet parent1 = selectParent(population);
                TimeTableSet parent2 = selectParent(population);
                TimeTableSet child = crossover(parent1, parent2);
                mutate(child);
                newGeneration.add(child);
            }

            population = newGeneration;
        }

        return population.getFirst();
    }


    public List<TimeTableSet> generateInitialPopulation(Semester semester, int populationSize) {
        List<TimeTableSet> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            Map<Group, TimeTableDto> timetables = new HashMap<>();
            for (AcademicSemester academicSemester : semester.getAcademicSemester()) {
                timetables.put(academicSemester.getGroup(), generateRandomTimeTable(academicSemester));
            }
            population.add(new TimeTableSet(timetables, calculateFitness(timetables)));
        }

        return population;
    }

    public TimeTableDto generateRandomTimeTable(@NotNull AcademicSemester academicSemester) {
        Group group = academicSemester.getGroup();

        TimeTableDto timeTable = TimeTableDto.getInstance(academicSemester, group);

        List<TeacherCourse> teacherCourses = academicSemester.getTeacherCourse();

        Map<DayOfWeek, Set<Slot>> availableSlots = createFreeTimeTableMap();

        for (TeacherCourse teacherCourse : teacherCourses) {
            Course course = teacherCourse.getCourse();
            Teacher teacher = teacherCourse.getTeacher();
            ClassRoom classRoom = getRandomItem(classRoomService.findByType(course.getClassRoomType()));

            SlotInDay slotInDay = randomSlotInDay(getWorkWeekDays(), availableSlots);

            if (availableSlots.containsKey(slotInDay.dayOfWeek)) {
                availableSlots.get(slotInDay.dayOfWeek).remove(slotInDay.slot);
            }
            if (availableSlots.containsKey(slotInDay.dayOfWeek) && availableSlots.get(slotInDay.dayOfWeek).isEmpty()) {
                availableSlots.remove(slotInDay.dayOfWeek);
            }
            timeTable.days()
                    .get(slotInDay.dayOfWeek)
                    .timeSlots().put(slotInDay.slot, new TimeSlot(teacher.getId(), course.getId(), classRoom.getId(), group.getId()));
        }
        return timeTable;
    }

    private int calculateFitness(Map<Group, TimeTableDto> timetables) {
        int score = 0;
        Set<String> usedTeachers = new HashSet<>();
        Set<String> usedClassrooms = new HashSet<>();

        for (TimeTableDto timetable : timetables.values()) {
            for (Map.Entry<DayOfWeek, Day> dayEntry : timetable.days().entrySet()) {
                for (Slot slot : dayEntry.getValue().timeSlots().keySet()) {
                    TimeSlot timeSlot = dayEntry.getValue().timeSlots().get(slot);
                    String teacherKey = dayEntry.getKey() + "_" + slot + "_" + timeSlot.teacherId();
                    String roomKey = dayEntry.getKey() + "_" + slot + "_" + timeSlot.classRoomId();

                    if (!usedTeachers.contains(teacherKey)) {
                        score += 10; // Reward if teacher is not double-booked
                        usedTeachers.add(teacherKey);
                    } else {
                        score -= 50; // Penalize conflicts
                    }

                    if (!usedClassrooms.contains(roomKey)) {
                        score += 10; // Reward if classroom is not double-booked
                        usedClassrooms.add(roomKey);
                    } else {
                        score -= 50; // Penalize conflicts
                    }
                }
            }
        }

        return score;
    }

    private void mutate(TimeTableSet timeTableSet) {
        Random random = new Random();

        for (Map.Entry<Group, TimeTableDto> timeTableDtoEntry : timeTableSet.timetables().entrySet()) {
            for (Map.Entry<DayOfWeek, Day> dayEntry : timeTableDtoEntry.getValue().days().entrySet()) {
                for (Slot slot : new ArrayList<>(dayEntry.getValue().timeSlots().keySet())) {
                    if (Math.random() < 0.1) { // 10% chance to mutate
                        TimeSlot timeSlot = dayEntry.getValue().timeSlots().get(slot);

                        // Select a new random day
                        DayOfWeek newDay = getRandomItem(getWorkWeekDays());

                        // Select a new random slot
                        Slot newSlot = getRandomItem(Arrays.stream(Slot.values()).toList());

                        // Select a new classroom (of the same type)
                        List<ClassRoom> availableRooms = null;
                        try {
                            availableRooms = classRoomService.findByType(courseService.findById(timeSlot.courseId()).getClassRoomType());
                        } catch (NotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        if (availableRooms == null || availableRooms.isEmpty()) {
                            return;
                        }

                        ClassRoom newClassRoom = getRandomItem(availableRooms);

                        // Remove the old entry and add the new one
                        dayEntry.getValue().timeSlots().remove(slot);
                        timeTableSet.timetables().get(timeTableDtoEntry.getKey())
                                .days().get(newDay)
                                .timeSlots().put(newSlot, new TimeSlot(
                                        timeSlot.teacherId(),
                                        timeSlot.courseId(),
                                        newClassRoom.getId(),
                                        timeSlot.groupId()
                                ));
                    }
                }
            }
        }
    }


    private TimeTableSet crossover(TimeTableSet parent1, TimeTableSet parent2) {
        Map<Group, TimeTableDto> newTimetables = new HashMap<>();

        for (Group group : parent1.timetables().keySet()) {
            if (Math.random() < 0.5) {
                newTimetables.put(group, parent1.timetables().get(group));
            } else {
                newTimetables.put(group, parent2.timetables().get(group));
            }
        }

        return new TimeTableSet(newTimetables, calculateFitness(newTimetables));
    }

    private TimeTableSet selectParent(List<TimeTableSet> population) {
        Random random = new Random();
        TimeTableSet best = null;
        int bestFitness = Integer.MIN_VALUE;

        for (int i = 0; i < 3; i++) {  // Tournament size = 3
            TimeTableSet candidate = population.get(random.nextInt(population.size()));
            if (candidate.fitness() > bestFitness) {
                bestFitness = candidate.fitness();
                best = candidate;
            }
        }

        return best;
    }

    private <T> T getRandomItem(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    private SlotInDay randomSlotInDay(List<DayOfWeek> days, Map<DayOfWeek, Set<Slot>> availableSlots) {
        DayOfWeek day = getRandomItem(availableSlots.keySet().stream().toList());
        Slot slot = getRandomItem(availableSlots.get(day).stream().toList());
        return new SlotInDay(day, slot);
    }

    private List<DayOfWeek> getWorkWeekDays() {
        return Arrays.stream(DayOfWeek.values())
                .filter(dayOfWeek -> !dayOfWeek.equals(DayOfWeek.SUNDAY)).toList();
    }
    private Map<DayOfWeek, Set<Slot>> createFreeTimeTableMap() {
        return getWorkWeekDays().stream().collect(Collectors.toMap(dayOfWeek -> dayOfWeek,
                dayOfWeek -> new HashSet<>(List.of(Slot.values()))));
    }

    record SlotInDay(
            DayOfWeek dayOfWeek,
            Slot slot
    ) {
    }

    public record TimeTableSet(
            Map<Group, TimeTableDto> timetables,
            int fitness
    ) {
    }

}
