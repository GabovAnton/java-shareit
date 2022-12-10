package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemLastBookingDto {
    @JsonView({ItemDto.SimpleView.class})
    private final Long id;
    @JsonView({ItemDto.SimpleView.class})
    private final Long bookerId;
}
