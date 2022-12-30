package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
public class RequestWithProposalsDto implements Serializable {

    private final Long id;

    private final String description;

    private final UserDto requester;

    private final LocalDateTime created;

    private List<ItemDto> items;

}