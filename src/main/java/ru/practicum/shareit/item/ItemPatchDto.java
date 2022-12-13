package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.User;


@Getter
@Setter
@AllArgsConstructor
public class ItemPatchDto {

    private long id;
    private String name;
    private String description;

    private Boolean available;

    private Long requestId;
}
