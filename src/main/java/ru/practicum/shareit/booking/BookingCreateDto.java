package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingCreateDto {

    private final Long id;

    private final LocalDateTime start;

    private final LocalDateTime end;

    private final String status;

    private final Long itemId;

    private final Long userId;
}