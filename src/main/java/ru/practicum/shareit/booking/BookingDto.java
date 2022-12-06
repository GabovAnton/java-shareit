package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * A DTO for the {@link Booking} entity
 */
@Getter
@Setter
@AllArgsConstructor
public class BookingDto {

    private final Long id;

    private final OffsetDateTime startDate;

    private final OffsetDateTime endDate;

    @NotNull
    private final String status;

    private final ItemDto item;

    private final UserDto booker;


}