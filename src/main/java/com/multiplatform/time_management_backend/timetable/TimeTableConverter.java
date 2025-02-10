package com.multiplatform.time_management_backend.timetable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.multiplatform.time_management_backend.timetable.modal.Day;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.util.Map;

@Converter
public class TimeTableConverter implements AttributeConverter<Map<DayOfWeek, Day>, String> {

    private final ObjectMapper mapper;

    public TimeTableConverter(ObjectMapper mapper) {
        this.mapper = mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    @Override
    public String convertToDatabaseColumn(Map<DayOfWeek, Day> dayOfWeekDayMap) {
        if (dayOfWeekDayMap == null || dayOfWeekDayMap.isEmpty()) {
            return "{}";
        }
        try {
            return mapper.writeValueAsString(dayOfWeekDayMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<DayOfWeek, Day> convertToEntityAttribute(String string) {
        if (StringUtils.isBlank(string)) {
            return Map.of();
        }
        JavaType type = mapper.getTypeFactory()
                .constructMapType(Map.class, DayOfWeek.class, Day.class);
        try {
            return mapper.readValue(string, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
