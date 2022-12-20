package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemProposalDto {
    private final Long id;

    private final Long itemId;

    private final String itemName;

    private final Long requesterId;
}
