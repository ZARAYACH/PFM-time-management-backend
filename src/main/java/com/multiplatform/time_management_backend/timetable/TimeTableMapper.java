package com.multiplatform.time_management_backend.timetable;

import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import com.multiplatform.time_management_backend.timetable.modal.TimeTableDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface TimeTableMapper {

    TimeTableDto toTimeTableDto(TimeTable timeTable);

    List<TimeTableDto> toTimeTableDto(List<TimeTable> timeTables);

}
