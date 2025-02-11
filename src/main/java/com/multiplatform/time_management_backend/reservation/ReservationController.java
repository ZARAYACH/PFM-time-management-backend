package com.multiplatform.time_management_backend.reservation;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.reservation.modal.Reservation;
import com.multiplatform.time_management_backend.reservation.modal.dto.ReservationDto;
import com.multiplatform.time_management_backend.reservation.service.ReservationService;
import com.multiplatform.time_management_backend.user.model.User;
import com.multiplatform.time_management_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/reservations")
@RestController
@RequiredArgsConstructor
@Tag(name = "Reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final UserService userService;

    @GetMapping
    public List<ReservationDto> getAllReservations() {
        return reservationMapper.toReservationDto(reservationService.list());
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

    @DeleteMapping("{id}")
    public Map<String, Boolean> deleteReservation(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "false") boolean deleteRecurrences) throws BadArgumentException, NotFoundException {
        Reservation reservation = reservationService.findById(id);
        reservationService.delete(reservation, deleteRecurrences);
        return Collections.singletonMap("deleted", true);
    }

}
