package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.user.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link ru.practicum.shareit.request.Request} entity
 */
@Data
@Builder
public class RequestDto implements Serializable {

    private final Long id;

    private final String description;

    private final UserDto requester;

    private final LocalDateTime createdDate;

}