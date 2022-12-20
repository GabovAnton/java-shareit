package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link Request} entity
 */
@Data
@Builder
public class RequestWithProposalsDto implements Serializable {

    private final Long id;

    private final String description;

    private final UserDto requester;

    private final LocalDateTime createdDate;

    private Set<ItemProposalDto> itemProposals;

}