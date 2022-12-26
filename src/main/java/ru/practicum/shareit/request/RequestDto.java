package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.UserDto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.request.Request} entity
 */
@Data
@Builder
public class RequestDto implements Serializable {

    private final Long id;

    @NotBlank
    private final String description;

    private final UserDto requester;

    private final LocalDateTime created;

}