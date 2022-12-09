package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ConflictException extends RuntimeException {
    public ConflictException(String s) {
        super(s);
    }
}

