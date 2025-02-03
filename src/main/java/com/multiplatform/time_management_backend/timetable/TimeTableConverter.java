package com.multiplatform.time_management_backend.timetable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multiplatform.time_management_backend.timetable.modal.TimeTable;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
@Converter
public class TimeTableConverter implements AttributeConverter<TimeTable, String> {

    private final ObjectMapper mapper;

    @Override
    public String convertToDatabaseColumn(TimeTable timeTable) {
        if (timeTable == null) {
            return "{}";
        }
        return mapper.convertValue(timeTable, String.class);
    }

    @Override
    public TimeTable convertToEntityAttribute(String string) {
        if (StringUtils.isBlank(string)) {
            return null;
        }
        return mapper.convertValue(string, TimeTable.class);
    }
}
