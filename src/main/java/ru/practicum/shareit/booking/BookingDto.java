package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
public class BookingDto implements Serializable {

    private final Long id;

    private final LocalDateTime start;

    private final LocalDateTime end;

    private final BookingStatus status;

    private final ItemDto item;

    private final UserDto booker;
}