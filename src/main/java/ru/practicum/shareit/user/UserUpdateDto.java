package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.ItemDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
public class UserUpdateDto implements Serializable {

    private final Long id;

    private final Set<ItemDto> items;

    private final Set<CommentDto> Comments;

    private final String name;

    private final String email;

    private final LocalDate registrationDate;

}