package com.multiplatform.time_management_backend.reservation.controller;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.reservation.ReservationMapper;
import com.multiplatform.time_management_backend.reservation.modal.Reservation;
import com.multiplatform.time_management_backend.reservation.modal.dto.ReservationDto;
import com.multiplatform.time_management_backend.reservation.service.ReservationService;
import com.multiplatform.time_management_backend.semester.modal.Semester;
import com.multiplatform.time_management_backend.semester.service.SemesterService;
import com.multiplatform.time_management_backend.user.model.User;
import com.multiplatform.time_management_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/reservations")
@RestController
@RequiredArgsConstructor
@Tag(name = "Reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final UserService userService;
    private final SemesterService semesterService;

    @GetMapping
    public List<ReservationDto> getAllReservations(@RequestParam(required = false) Long semesterId) throws NotFoundException {
        if (semesterId != null) {
            Semester semester = semesterService.findById(semesterId);
            return reservationMapper.toReservationDto(reservationService.list(semester));
        }
        return reservationMapper.toReservationDto(reservationService.list());
    }
    @GetMapping("/{id}")
    public ReservationDto getReservationById(@PathVariable long id) throws NotFoundException {
        return reservationMapper.toReservationDto(reservationService.findById(id));
    }

    @PostMapping
    public ReservationDto createReservation(@RequestBody ReservationDto reservationDto, @AuthenticationPrincipal UserDetails userDetails) throws BadArgumentException, NotFoundException {
        User user = userService.findByEmail(userDetails.getUsername());
        return reservationMapper.toReservationDto(reservationService.create(reservationDto, user));
    }

    @PutMapping("{id}")
    public ReservationDto updateReservation(@PathVariable Long id,
                                            @RequestBody ReservationDto reservationDto,
                                            @RequestParam(defaultValue = "false") boolean updateRecurrences) throws BadArgumentException, NotFoundException {
        Reservation reservation = reservationService.findById(id);
        return reservationMapper.toReservationDto(reservationService.modify(reservation, reservationDto, updateRecurrences));
    }

    @DeleteMapping("{ids}")
    public Map<String, Boolean> deleteReservations(@PathVariable Long[] ids,
                                                  @RequestParam(defaultValue = "false") boolean deleteRecurrences) throws BadArgumentException, NotFoundException {
        List<Reservation> reservations = reservationService.findById(Arrays.stream(ids).collect(Collectors.toSet()));
        reservationService.delete(reservations, deleteRecurrences);
        return Collections.singletonMap("deleted", true);
    }

}
