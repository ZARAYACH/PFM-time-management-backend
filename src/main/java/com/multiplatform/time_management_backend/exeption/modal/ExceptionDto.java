package com.multiplatform.time_management_backend.exeption.modal;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.random.RandomGenerator;

@Getter
@RequiredArgsConstructor
@Builder
public class ExceptionDto {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;
    private final Integer status;
    private final String statusDescription;
    private final String errorId = RandomStringUtils.random(10, 0, 0, true, true, null, Random.from(RandomGenerator.getDefault()));

}
