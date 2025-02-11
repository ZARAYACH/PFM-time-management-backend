package com.multiplatform.time_management_backend.reservation;

import com.multiplatform.time_management_backend.reservation.modal.Reservation;
import com.multiplatform.time_management_backend.reservation.modal.dto.ReservationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
@Component
public interface ReservationMapper {
    List<ReservationDto> toReservationDto(List<Reservation> reservationList);

    @Mapping(source = "reservedBy.id", target = "reservedBy")
    @Mapping(source = "classroom.id", target = "classroomId")
    @Mapping(source = "parentReservation.id", target = "parentReservationId")
    @Mapping(source = "childReservations", target = "childReservationIds")
    @Mapping(source = "academicClass.id", target = "classId")
    ReservationDto toReservationDto(Reservation reservation);


    default Long map(Reservation reservation) {
        return reservation.getId();
    }

    ;
}
