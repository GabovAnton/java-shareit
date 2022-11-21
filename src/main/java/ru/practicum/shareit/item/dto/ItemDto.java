package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Builder
@Data
@AllArgsConstructor
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private boolean isAvailable;
    private User owner;

    private Integer requestId;

}

