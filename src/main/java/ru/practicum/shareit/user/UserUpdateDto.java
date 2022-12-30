package ru.practicum.shareit.user;

import lombok.*;
import ru.practicum.shareit.item.CommentDto;
import ru.practicum.shareit.item.ItemDto;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto implements Serializable {

    private Long id;

    private Set<ItemDto> items;

    private Set<CommentDto> Comments;

    private String name;

    private String email;

    private String registrationDate;

}