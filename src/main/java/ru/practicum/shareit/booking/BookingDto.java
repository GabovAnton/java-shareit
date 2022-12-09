package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.booking.Booking} entity
 */
@Data
public class BookingDto implements Serializable {
    private final Long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final BookingStatus status;
    private final ItemDto item;
    private final UserDto booker;

    /**
     * A DTO for the {@link ru.practicum.shareit.item.Item} entity
     */
    @Data
    public static class ItemDto implements Serializable {
        private final Long id;
        private final String name;
        private final String description;
        private final Boolean available;
        private final Integer requestId;
    }

    /**
     * A DTO for the {@link ru.practicum.shareit.user.User} entity
     */
    @Data
    public static class UserDto implements Serializable {
        private final Long id;
        private final String name;
        private final String email;
        private final LocalDate registrationDate;
    }
}