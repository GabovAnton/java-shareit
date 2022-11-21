package ru.practicum.shareit.item.dto;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */

@Builder
@Data
@AllArgsConstructor
public class ItemDto {
    private String name;
    private String description;
    private boolean isAvailable;

    private Integer requestId;



}
