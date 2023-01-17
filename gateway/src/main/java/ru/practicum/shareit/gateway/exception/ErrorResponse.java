package ru.practicum.shareit.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ErrorResponse {

    @Getter
    private final String error;

}