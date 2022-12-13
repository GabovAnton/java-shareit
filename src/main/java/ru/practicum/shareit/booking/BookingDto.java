package ru.practicum.shareit.booking;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class BookingDto implements Serializable {
    private final Long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final BookingStatus status;
    private final ItemDto item;
    private final UserDto booker;


    @Data
    public static class ItemDto implements Serializable {
        private final Long id;
        private final String name;
        private final String description;
        private final Boolean available;
        private final Integer requestId;
    }

    @Data
    public static class UserDto implements Serializable {
        private final Long id;
        private final String name;
        private final String email;
        private final LocalDate registrationDate;
    }
}