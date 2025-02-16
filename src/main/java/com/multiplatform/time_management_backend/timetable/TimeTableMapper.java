package com.multiplatform.time_management_backend.timetable;

import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import com.multiplatform.time_management_backend.timetable.modal.TimeTableDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface TimeTableMapper {
    @Mapping(source = "semester.id", target = "semesterId")
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.name", target = "groupName")
    TimeTableDto toTimeTableDto(TimeTable timeTable);

    List<TimeTableDto> toTimeTableDto(List<TimeTable> timeTables);

}
