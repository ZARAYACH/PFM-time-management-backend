package com.multiplatform.time_management_backend.reservation.service;

import com.multiplatform.time_management_backend.academicclass.modal.AcademicClass;
import com.multiplatform.time_management_backend.academicclass.repository.AcademicClassRepository;
import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.reservation.modal.Reservation;
import com.multiplatform.time_management_backend.reservation.modal.dto.ReservationDto;
import com.multiplatform.time_management_backend.reservation.repository.ReservationRepository;
import com.multiplatform.time_management_backend.room.model.ClassRoom;
import com.multiplatform.time_management_backend.room.repository.ClassRoomRepository;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.timetable.modal.Day;
import com.multiplatform.time_management_backend.timetable.modal.Slot;
import com.multiplatform.time_management_backend.timetable.modal.TimeSlot;
import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import com.multiplatform.time_management_backend.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClassRoomRepository classRoomRepository;
    private final AcademicClassRepository academicClassRepository;

    public List<Reservation> list() {
        return reservationRepository.findAll();
    }

    public Reservation create(ReservationDto reservationDto, User user) throws BadArgumentException {
        Reservation reservation = validateReservationDtoAndCreate(reservationDto);
        reservation.setReservedBy(user);
        reservation.getChildReservations().forEach(reservation1 -> reservation1.setReservedBy(user));
        return saveReservation(reservation);
    }

    private Reservation validateReservationDtoAndCreate(ReservationDto reservationDto) throws BadArgumentException {
        Reservation reservation = new Reservation();
        try {
            Assert.notNull(reservationDto.classroomId(), "Classroom cannot be null");
            Assert.notNull(reservationDto.reservedBy(), "ReservedBy cannot be null");
            Assert.notNull(reservationDto.startTime(), "Start time cannot be null");
            Assert.notNull(reservationDto.endTime(), "End time cannot be null");
            Assert.isTrue(!reservationDto.startTime().isAfter(reservationDto.endTime()), "Start time must be before end time");

            reservation.setClassroom(classRoomRepository.findById(reservationDto.classroomId())
                    .orElseThrow(() -> new IllegalArgumentException("Classroom with id " + reservationDto.classroomId() + " not found")));

            reservation.setStartTime(reservationDto.startTime());
            reservation.setEndTime(reservationDto.endTime());
            reservation.setType(reservationDto.type());

            if (reservationDto.recurring()) {
                if (reservationDto.recurrenceCount() == null && reservationDto.recurrenceEndDate() == null) {
                    throw new IllegalArgumentException("Either recurrence count or recurrence end date must be provided for recurring reservations");
                }

                if (reservationDto.recurrenceEndDate() != null) {
                    Assert.isTrue(!reservationDto.startTime().isAfter(reservationDto.recurrenceEndDate()), "Recurrence end date must be after start time");
                    reservation.setRecurrenceEndDate(reservationDto.recurrenceEndDate());
                }

                if (reservationDto.recurrenceCount() != null) {
                    reservation.setRecurrenceCount(reservationDto.recurrenceCount());
                }
            }

        } catch (IllegalArgumentException e) {
            throw new BadArgumentException(e);
        }
        return reservation;
    }


    private Reservation saveReservation(Reservation reservation) throws BadArgumentException {
        if (hasConflict(reservation)) {
            throw new BadArgumentException("Time slot is already reserved!");
        }
        if (reservation.isRecurring()) {
            reservation.setChildReservations(createLimitedRecurringReservations(reservation));
        }
        return reservationRepository.save(reservation);
    }

    private boolean hasConflict(Reservation reservation) {
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                reservation.getClassroom().getId(),
                reservation.getStartTime(),
                reservation.getEndTime());
        return !conflicts.isEmpty();
    }

    private List<Reservation> createLimitedRecurringReservations(Reservation reservation) {
        LocalDateTime nextStart = reservation.getStartTime();
        LocalDateTime nextEnd = reservation.getEndTime();

        List<Reservation> recurringReservations = new ArrayList<>();

        int count = 0;
        long limitCount;
        int recurrenceDurationInMonths = 12;

        if (reservation.getRecurrenceCount() != null) {
            limitCount = reservation.getRecurrenceCount();
        } else {
            LocalDateTime nowPlusLimit = LocalDateTime.now().plusMonths(recurrenceDurationInMonths);
            LocalDateTime recurrenceEndDate = reservation.getRecurrenceEndDate();

            boolean hasValidEndDate = recurrenceEndDate != null && recurrenceEndDate.isBefore(nowPlusLimit);

            limitCount = switch (reservation.getType()) {
                case DAILY -> hasValidEndDate ?
                        ChronoUnit.DAYS.between(recurrenceEndDate, LocalDateTime.now()) :
                        recurrenceDurationInMonths;
                case WEEKLY -> hasValidEndDate ?
                        ChronoUnit.WEEKS.between(recurrenceEndDate, LocalDateTime.now()) :
                        4 * recurrenceDurationInMonths;
                case MONTHLY -> hasValidEndDate ?
                        ChronoUnit.MONTHS.between(recurrenceEndDate, LocalDateTime.now()) :
                        recurrenceDurationInMonths;
                default -> 0;
            };
        }

        while (count < limitCount) {
            switch (reservation.getType()) {
                case DAILY:
                    nextStart = nextStart.plusDays(1);
                    nextEnd = nextEnd.plusDays(1);
                    break;
                case WEEKLY:
                    nextStart = nextStart.plusWeeks(1);
                    nextEnd = nextEnd.plusWeeks(1);
                    break;
                case MONTHLY:
                    nextStart = nextStart.plusMonths(1);
                    nextEnd = nextEnd.plusMonths(1);
                    break;
                default:
                    return Collections.emptyList();
            }

            if (nextStart.isAfter(reservation.getStartTime().plusMonths(recurrenceDurationInMonths))) {
                break;
            }

            Reservation newReservation = Reservation.builder()
                    .classroom(reservation.getClassroom())
                    .reservedBy(reservation.getReservedBy())
                    .academicClass(reservation.getAcademicClass())
                    .startTime(nextStart)
                    .endTime(nextEnd)
                    .type(Reservation.Type.NONE)
                    .parentReservation(reservation)
                    .build();

            if (!hasConflict(newReservation)) {
                newReservation.setParentReservation(reservation);
                recurringReservations.add(newReservation);
            }
            count++;
        }
        return recurringReservations;
    }

    public Reservation modify(Reservation existingReservation, ReservationDto updatedReservationDto, boolean updateRecurrences) throws BadArgumentException {

        Reservation updatedReservation = validateReservationDtoAndCreate(updatedReservationDto);

        if (updateRecurrences && existingReservation.getParentReservation() != null) {
            Reservation parentReservation = existingReservation.getParentReservation();
            parentReservation.setStartTime(updatedReservation.getStartTime());
            parentReservation.setEndTime(updatedReservation.getEndTime());
            parentReservation.setType(updatedReservation.getType());
            reservationRepository.save(parentReservation);
        }

        if (updateRecurrences && existingReservation.isRecurring()) {
            List<Reservation> futureReservations = reservationRepository.findByParentReservationAndStartTimeAfter(
                    existingReservation, LocalDateTime.now());

            for (Reservation futureReservation : futureReservations) {
                futureReservation.setStartTime(updatedReservation.getStartTime());
                futureReservation.setEndTime(updatedReservation.getEndTime());
                futureReservation.setType(updatedReservation.getType());
            }
            reservationRepository.saveAll(futureReservations);
        }

        return reservationRepository.save(existingReservation);
    }

    public void delete(Reservation reservation, boolean deleteRecurrences) throws BadArgumentException {
        if (!deleteRecurrences) {
            reservationRepository.saveAllAndFlush(reservation.getChildReservations()
                    .stream().peek(childReservation -> childReservation.setParentReservation(null)).collect(Collectors.toSet()));
            reservation.setChildReservations(null);
        }
        reservation.setAcademicClass(null);
        reservation.setParentReservation(null); ;
        reservation.setClassroom(null);
        reservation.setReservedBy(null);
        reservationRepository.delete(reservation);
    }

    public Reservation findById(Long id) throws NotFoundException {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation #" + id + " Not found"));
    }

    public List<Reservation> reserveSemester(@NotNull Semester semester, User user) throws NotFoundException {
        try {
            Assert.notEmpty(semester.getTimeTables(), "Semester must not be nullor empty");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        List<Reservation> reservationList = new ArrayList<>();
        List<TimeTable> timeTables = semester.getTimeTables();

        for (TimeTable timeTable : timeTables) {
            reservationList.addAll(reserveTimeTable(timeTable, semester.getEndDate()));
        }
        return reservationRepository.saveAllAndFlush(reservationList.stream()
                .peek(reservation -> reservation.setReservedBy(user))
                .collect(Collectors.toSet()));
    }

    private List<Reservation> reserveTimeTable(TimeTable timeTable, LocalDate recurrenceEndDate) throws NotFoundException {
        List<Reservation> reservations = new ArrayList<>();
        for (Map.Entry<DayOfWeek, Day> dayEntry : timeTable.getDays().entrySet()) {
            LocalDateTime nextDay = LocalDateTime.now()
                    .with(TemporalAdjusters.next(dayEntry.getKey()))
                    .withHour(0).withMinute(0).withSecond(0).withNano(0);
            for (Map.Entry<Slot, TimeSlot> timeSlotEntry : dayEntry.getValue().timeSlots().entrySet()) {

                ClassRoom classRoom = classRoomRepository.findById(timeSlotEntry.getValue().classRoomId())
                        .orElseThrow(() -> new NotFoundException("Classroom #" + timeSlotEntry.getValue().classRoomId() + "Not found"));
                AcademicClass academicClass = academicClassRepository.findById(timeSlotEntry.getValue().academicClassId())
                        .orElseThrow(() -> new NotFoundException("Academic class #" + timeSlotEntry.getValue().academicClassId() + "Not found"));
                Reservation reservation = Reservation.builder()
                        .academicClass(academicClass)
                        .classroom(classRoom)
                        .startTime(timeSlotEntry.getKey().getStartTime().atDate(nextDay.toLocalDate()))
                        .endTime(timeSlotEntry.getKey().getEndTime().atDate(nextDay.toLocalDate()))
                        .recurrenceEndDate(recurrenceEndDate.atStartOfDay())
                        .type(Reservation.Type.WEEKLY).build();
                reservations.add(reservation);
                reservations.addAll(createLimitedRecurringReservations(reservation));
            }
        }
        return reservations;

    }
}

